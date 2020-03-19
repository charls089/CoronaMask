package com.kobbi.project.coronamask.network.retrofit.response

import com.google.gson.annotations.SerializedName
import com.kobbi.project.coronamask.network.retrofit.domain.StoreDetail

data class StoreDetailResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("stores")
    val stores: List<StoreDetail>
)