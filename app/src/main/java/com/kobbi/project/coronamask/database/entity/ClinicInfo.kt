package com.kobbi.project.coronamask.database.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "ClinicInfo",
    primaryKeys = ["code", "name"],
    indices = [Index("province", "city", "name")]
)
data class ClinicInfo(
    val code: String,
    val province: String,
    val city: String,
    val name: String,
    val address: String,
    val tel: String,
    val type: String,
    val latitude: Double,
    val longitude: Double
)