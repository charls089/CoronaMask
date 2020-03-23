package com.kobbi.project.coronamask.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kobbi.project.coronamask.database.converter.DataConverter
import com.kobbi.project.coronamask.database.dao.ClinicInfoDAO
import com.kobbi.project.coronamask.database.entity.ClinicInfo
import com.kobbi.project.coronamask.util.DLog
import com.kobbi.project.coronamask.util.SharedPrefHelper
import java.util.concurrent.Executors
import kotlin.concurrent.thread

@Database(
    entities = [ClinicInfo::class],
    version = 1
)
@TypeConverters(DataConverter::class)
abstract class ClinicDatabase : RoomDatabase() {
    abstract fun clinicInfoDao(): ClinicInfoDAO

    companion object {
        private const val TAG = "ClinicDatabase"
        private const val DB_NAME = "ClinicInformation.db"

        @Volatile
        private var INSTANCE: ClinicDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): ClinicDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, ClinicDatabase::class.java, DB_NAME)
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            DLog.v(tag = TAG, message = "onCreate()")
                        }
                    })
                    .build().also {
                        INSTANCE = it
                    }
            }
        }

        @JvmStatic
        fun initializeDB(context: Context, listener: CompleteListener) {
            val init = SharedPrefHelper.getBool(context, SharedPrefHelper.KEY_DB_INITIALIZED)
            if (!init) {
                thread {
                    setDatabaseData(context, listener)
                    listener.onComplete()
                }
            } else {
                listener.onComplete()
            }
        }

        private fun setDatabaseData(context: Context, listener: CompleteListener) {
            AddressFile.values().forEach { file ->
                getDataFromAsset(context, file, listener).let {
                    Executors.newSingleThreadScheduledExecutor().execute {
                        getDatabase(context).clinicInfoDao().insert(it)
                        SharedPrefHelper.setBool(
                            context,
                            SharedPrefHelper.KEY_DB_INITIALIZED,
                            true
                        )
                    }
                }
            }
        }

        private fun getDataFromAsset(
            context: Context,
            file: AddressFile,
            listener: CompleteListener
        ): List<ClinicInfo> {
            val results = mutableListOf<ClinicInfo>()
            String.format("info_%s_20200320.csv", file.fileName).let { fileName ->
                context.applicationContext.assets.open(fileName).use { ips ->
                    ips.bufferedReader().use { br ->
                        val dataList = br.readLines()
                        listener.onStart(fileName, dataList.size - 1)
                        try {
                            for (i in 1 until dataList.size) {
                                val data = splitCsvString(dataList[i])
                                if (data.size == 9)
                                    results.add(
                                        ClinicInfo(
                                            data[0],
                                            data[1],
                                            data[2],
                                            data[3],
                                            data[4],
                                            data[5],
                                            data[6],
                                            data[7].toDouble(),
                                            data[8].toDouble()
                                        )
                                    )
                                listener.onLoad(i)
                            }
                        } catch (e: Exception) {
                            listener.onError(e)
                        }
                    }
                }
            }
            return results
        }

        fun splitCsvString(csvString: String): List<String> {
            return if (csvString.contains("\"")) {
                val splitIndices = mutableListOf<Int>().apply {
                    add(-1)
                    var dQuoteCount = 0
                    csvString.forEachIndexed { index, c ->
                        if (c == ',') {
                            if (csvString[index + 1] == '"') {
                                add(index)
                                dQuoteCount++
                            } else {
                                if (dQuoteCount == 0)
                                    add(index)
                            }
                        } else if (c == '"') {
                            if (index + 1 < csvString.length && csvString[index + 1] == ',')
                                dQuoteCount = 0
                        }
                    }
                    add(csvString.length)
                }
                mutableListOf<String>().apply {
                    for (i in 0 until splitIndices.size - 1) {
                        val data = csvString.substring(splitIndices[i] + 1, splitIndices[i + 1])
                        add(data.replace("\"", ""))
                    }

                }
            } else {
                csvString.split(',')
            }
        }
    }

    enum class AddressFile(val fileName: String) {
        SAFETY_HOSPITAL_ADDRESS("safety_hospital"),
        SELECTED_CLINIC_ADDRESS("selected_clinic"),
        DRIVE_THROUGH("drive_through")
    }

    interface CompleteListener {
        fun onStart(fileName: String, totalCount: Int)
        fun onLoad(count: Int)
        fun onComplete()
        fun onError(t: Throwable)
    }
}