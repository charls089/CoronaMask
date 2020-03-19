package com.kobbi.project.coronamask.network.retrofit.domain

import com.google.gson.annotations.SerializedName

data class StoreInfo(
    @SerializedName("addr")
    val address: String,
    @SerializedName("code")
    val code: Long,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String
)