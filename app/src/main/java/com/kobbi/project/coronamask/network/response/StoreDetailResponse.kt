package com.kobbi.project.coronamask.network.response

import com.google.gson.annotations.SerializedName
import com.kobbi.project.coronamask.network.domain.StoreDetail

data class StoreDetailResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("stores")
    val stores: List<StoreDetail>
)