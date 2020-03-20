package com.kobbi.project.coronamask.model

data class Clinic(
    val province: String,
    val city: String,
    val name: String,
    val address: String,
    val tel: String,
    val enableCollect: Boolean,
    val latitude: Double,
    val longitude: Double,
    val distance: Float?
)