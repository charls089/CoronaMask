package com.kobbi.project.coronamask.util

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.kobbi.project.coronamask.database.ClinicDatabase
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utils private constructor() {
    enum class DateType(
        val dateFormat: String
    ) {
        DEFAULT("%d월 %d일 (%s)"),
        ALL("%d월 %d일 (%s) %s %s시 %s분"),
        SHORT("%d/%d %s %s:%s")
    }

    companion object {
        const val VALUE_TIME_FORMAT = "HH:mm:ss"
        const val VALUE_DATE_FORMAT = "yyyyMMdd"
        const val VALUE_DATE_FORMAT2 = "yyyy년 MM월 dd일"
        const val VALUE_DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss"
        const val VALUE_DATETIME_FORMAT2 = "yyyy년 MM월 dd일, HH:mm:ss"

        @JvmStatic
        fun getCurrentTime(
            format: String = VALUE_DATE_FORMAT, time: Long = System.currentTimeMillis()
        ): String =
            SimpleDateFormat(format, Locale.getDefault()).format(time)


        @JvmStatic
        fun convertDateTime(
            time: Long = System.currentTimeMillis(),
            type: DateType = DateType.DEFAULT
        ): String {
            GregorianCalendar().run {
                timeInMillis = time

                val month = get(Calendar.MONTH)
                val date = get(Calendar.DATE)
                val dayOfWeek = when (get(Calendar.DAY_OF_WEEK)) {
                    Calendar.MONDAY -> "월"
                    Calendar.TUESDAY -> "화"
                    Calendar.WEDNESDAY -> "수"
                    Calendar.THURSDAY -> "목"
                    Calendar.FRIDAY -> "금"
                    Calendar.SATURDAY -> "토"
                    Calendar.SUNDAY -> "일"
                    else -> "ERROR"
                }
                val amPm = when (get(Calendar.AM_PM)) {
                    Calendar.AM -> "오전"
                    else -> "오후"
                }

                val hour =
                    convertTimeToString(if (get(Calendar.HOUR) == 0) 12 else get(Calendar.HOUR))
                val minute = convertTimeToString(get(Calendar.MINUTE))
                return when (type) {
                    DateType.DEFAULT -> {
                        String.format(type.dateFormat, month + 1, date, dayOfWeek)
                    }
                    DateType.ALL -> {
                        String.format(
                            type.dateFormat, month + 1, date, dayOfWeek, amPm, hour, minute
                        )
                    }
                    DateType.SHORT -> {
                        String.format(
                            type.dateFormat, month + 1, date, amPm, hour, minute
                        )
                    }
                }
            }
        }

        @JvmStatic
        fun setHideSoftInput(view: View) {
            view.context?.run {
                val manager =
                    applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                manager?.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        @JvmStatic
        fun replaceSign(string: String): String {
            return "\\W".toRegex().replace(string, "").take(8)
        }

        @JvmStatic
        fun convertStringToDate(format: String = VALUE_DATETIME_FORMAT, date: String?): Date? {
            return date?.run {
                try {
                    SimpleDateFormat(format, Locale.getDefault()).parse(date)
                } catch (e: ParseException) {
                    e.printStackTrace()
                    null
                }
            }
        }

        /**
         * @param center 사용자 위치 혹은 검색한 위치
         * @param end 주소의 위치(도착점의 위치)
         *
         * @return 두 지점간의 거리
         */
        @JvmStatic
        fun distanceToPoint(center: Location, end: Location): Int {
            return center.distanceTo(end).toInt()
        }

        private fun convertTimeToString(time: Int): String {
            return when (time) {
                in 0..9 -> "0$time"
                else -> time.toString()
            }
        }

        fun addLatLngToCsvData(context: Context) {
            ClinicDatabase.AddressFile.values().forEach { file ->
                String.format("info_%s_20200320.csv", file.fileName).let { fileName ->
                    context.applicationContext.assets.open(fileName).use { ips ->
                        ips.bufferedReader().use { br ->
                            val readLines = br.readLines()
                            val totalCount = readLines.size
                            readLines.forEachIndexed { index, line ->
                                try {
                                    StringBuilder().run {
                                        ClinicDatabase.splitCsvString(line).let { splits ->
                                            splits.forEach { data ->
                                                append(data)
                                                append(',')
                                            }
                                            if (file == ClinicDatabase.AddressFile.DRIVE_THROUGH) {
                                                append("O")
                                                append(',')
                                            }
                                            Geocoder(context).getFromLocationName(splits[4], 1)
                                                ?.let { addressList ->
                                                    if (addressList.isNotEmpty())
                                                        addressList[0].run {
                                                            append(latitude)
                                                            append(',')
                                                            append(longitude)
                                                        }
                                                }
                                        }
                                        val dir =
                                            File(context.getExternalFilesDir(null), "log").apply {
                                                if (!this.exists()) {
                                                    this.mkdirs()
                                                }
                                            }
                                        val logFile = File(dir, fileName)
                                        try {
                                            val lineData = this.toString()
                                            FileOutputStream(logFile, true).use {
                                                if (logFile.length() > 0)
                                                    it.write("\r\n".toByteArray())
                                                it.write(lineData.toByteArray())
                                                Log.w(
                                                    "MakeCsvFile",
                                                    "data($index/$totalCount) : $lineData"
                                                )
                                            }
                                        } catch (e: FileNotFoundException) {
                                            Log.e("MakeCsvFile", "CSV File($fileName) Not Found.")
                                        }
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}