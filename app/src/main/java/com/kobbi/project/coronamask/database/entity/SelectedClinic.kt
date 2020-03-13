package com.kobbi.project.coronamask.database.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "SelectedClinic",
    primaryKeys = ["code"],
    indices = [Index("province", "city", "name")]
)
data class SelectedClinic(
    val code: Int,
    val enableCollect: Boolean,
    val province: String,
    val city: String,
    val name: String,
    val address: String,
    val tel: String
)