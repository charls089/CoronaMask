package com.kobbi.project.coronamask.network.retrofit.domain

import com.google.gson.annotations.SerializedName

data class SalesInfo (
    @SerializedName("code")
    val code:String,
    @SerializedName("stock_at")
    val stockAt:String,
    @SerializedName("remain_stat")
    val remainStat:String,
    @SerializedName("created_at")
    val createdAt:String
)