package com.kobbi.project.coronamask.network.retrofit.domain

import com.google.gson.annotations.SerializedName

data class StoreDetail(
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
    val type: String,
    @SerializedName("stock_at")
    val stockAt: String?,
    @SerializedName("remain_stat")
    val remainStat: String?,
    @SerializedName("created_at")
    val createdAt: String?
)