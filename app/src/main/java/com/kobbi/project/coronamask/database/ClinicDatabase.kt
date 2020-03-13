package com.kobbi.project.coronamask.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kobbi.project.coronamask.database.converter.DataConverter
import com.kobbi.project.coronamask.database.dao.SafetyHospitalDAO
import com.kobbi.project.coronamask.database.dao.SelectedClinicDAO
import com.kobbi.project.coronamask.database.entity.SafetyHospital
import com.kobbi.project.coronamask.database.entity.SelectedClinic
import com.kobbi.project.coronamask.util.SharedPrefHelper
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import kotlin.concurrent.thread

@Database(
    entities = [SelectedClinic::class, SafetyHospital::class],
    version = 1
)
@TypeConverters(DataConverter::class)
abstract class ClinicDatabase : RoomDatabase() {
    abstract fun safetyHospitalDao(): SafetyHospitalDAO
    abstract fun selectedClinicDao(): SelectedClinicDAO

    companion object {
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
                            Log.e("####", "ClinicDatabase.onCreate()")
                        }
                    })
                    .build().also {
                        INSTANCE = it
                    }
            }
        }

        @JvmStatic
        fun initializeDB(context: Context) {
            val init = SharedPrefHelper.getBool(context, SharedPrefHelper.KEY_DB_INITIALIZED)
            if (!init) {
                thread {
                    setDatabaseData(context)
                }
            }
            Log.e("####", "initializeDB()")
        }

        private fun setDatabaseData(context: Context) {
            AddressFile.values().forEach { file ->
                getDataFromAsset(context, file).let {
                    Executors.newSingleThreadScheduledExecutor().execute {
                        val database = getDatabase(context)
                        when (file) {
                            AddressFile.SAFETY_HOSPITAL_ADDRESS -> {
                                database.safetyHospitalDao()
                                    .insert(it.filterIsInstance(SafetyHospital::class.java))
                            }
                            AddressFile.SELECTED_CLINIC_ADDRESS -> {
                                database.selectedClinicDao()
                                    .insert(it.filterIsInstance(SelectedClinic::class.java))
                            }
                        }

                        SharedPrefHelper.setBool(
                            context,
                            SharedPrefHelper.KEY_DB_INITIALIZED,
                            true
                        )
                    }
                }
            }
        }

        private fun getDataFromAsset(context: Context, file: AddressFile): List<Any> {
            val results = mutableListOf<Any>()
            String.format("address_%s_20200311.csv", file.fileName).let { fileName ->
                context.applicationContext.assets.open(fileName).use { ips ->
                    ips.bufferedReader().use { br ->
                        val dataList = br.readLines()
                        for (i in 1 until dataList.size) {
                            val data = dataList[i].split(',')
                            results.add(
                                when (file) {
                                    AddressFile.SAFETY_HOSPITAL_ADDRESS -> {
                                        if (data.size == 8)
                                            SafetyHospital(
                                                data[0].toInt(),
                                                data[1],
                                                data[2],
                                                data[3],
                                                data[4],
                                                data[5],
                                                data[6],
                                                parseStringToDate(data[7])
                                            )
                                        else
                                            Any()
                                    }
                                    AddressFile.SELECTED_CLINIC_ADDRESS -> {
                                        if (data.size == 7)
                                            SelectedClinic(
                                                data[0].toInt(),
                                                convertYnToBoolean(data[1]),
                                                data[2],
                                                data[3],
                                                data[4],
                                                data[5],
                                                data[6]
                                            )
                                        else
                                            Any()
                                    }
                                }
                            )
                        }
                    }
                }
            }
            return results
        }

        private fun parseStringToDate(date: String): Date? {
            return try {
                SimpleDateFormat("yyyy.MM.dd.", Locale.getDefault()).parse(date)
            } catch (e: ParseException) {
                null
            }
        }

        private fun convertYnToBoolean(yn: String): Boolean {
            return yn == "Y"
        }
    }

    enum class AddressFile(val fileName: String) {
        SAFETY_HOSPITAL_ADDRESS("safety_hospital"),
        SELECTED_CLINIC_ADDRESS("selected_clinic")
    }
}