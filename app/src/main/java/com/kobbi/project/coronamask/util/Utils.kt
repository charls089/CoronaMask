package com.kobbi.project.coronamask.util

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
        const val VALUE_DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss"

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

        private fun convertTimeToString(time: Int): String {
            return when (time) {
                in 0..9 -> "0$time"
                else -> time.toString()
            }
        }
    }
}