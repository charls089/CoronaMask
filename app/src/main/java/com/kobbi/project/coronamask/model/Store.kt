package com.kobbi.project.coronamask.model

import java.util.*

data class Store(
    val address: String,
    val code: Long,
    val lat: Double,
    val lng: Double,
    val name: String,
    val type: String,
    val stockAt: Date?,
    val remainStat: RemainState,
    val distance: Int
)