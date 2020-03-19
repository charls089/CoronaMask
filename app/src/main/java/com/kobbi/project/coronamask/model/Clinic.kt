package com.kobbi.project.coronamask.model

data class Clinic(
    val code: Int,
    val enableCollect: Boolean,
    val province: String,
    val city: String,
    val name: String,
    val address: String,
    val tel: String,
    val distance: Float?
)