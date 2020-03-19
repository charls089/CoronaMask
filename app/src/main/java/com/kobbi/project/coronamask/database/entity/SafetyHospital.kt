package com.kobbi.project.coronamask.database.entity

import androidx.room.Entity
import androidx.room.Index
import java.util.*

@Entity(
    tableName = "SafetyHospital",
    primaryKeys = ["code", "type"],
    indices = [Index("province", "city", "name")]
)
data class SafetyHospital(
    val code: Int,
    val province: String,
    val city: String,
    val name: String,
    val address: String,
    val type: String,
    val tel: String,
    val date: Date? = null,
    val latitude: Double,
    val longitude: Double
)