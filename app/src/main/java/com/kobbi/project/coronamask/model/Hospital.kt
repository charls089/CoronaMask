package com.kobbi.project.coronamask.model

import java.util.*

data class Hospital(
    val code: Int,
    val province: String,
    val city: String,
    val name: String,
    val address: String,
    val type: String,
    val tel: String,
    val date: Date? = null,
    val distance: Float?
)