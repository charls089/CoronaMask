package com.kobbi.project.coronamask.model

data class Hospital(
    val province: String,
    val city: String,
    val name: String,
    val address: String,
    val type: String,
    val tel: String,
    val latitude: Double,
    val longitude: Double,
    val distance: Float?
)