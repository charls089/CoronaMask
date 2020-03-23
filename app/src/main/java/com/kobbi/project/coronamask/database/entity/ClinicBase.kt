package com.kobbi.project.coronamask.database.entity

import androidx.room.Entity
import java.util.*

@Entity(
    tableName = "ClinicBase",
    primaryKeys = ["code"]
)
data class ClinicBase(
    val code: String,
    val updateTime: Date?
)