package com.kobbi.project.coronamask.database.converter

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DataConverter {

    @TypeConverter
    fun StringToDate(date: String) =
        SimpleDateFormat("yyyy.MM.dd.", Locale.getDefault()).parse(date)

    @TypeConverter
    fun DateToString(date: Date) =
        SimpleDateFormat("yyyy.MM.dd.", Locale.getDefault()).format(date)
}