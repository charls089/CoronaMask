package com.kobbi.project.coronamask.network.response

import com.google.gson.annotations.SerializedName
import com.kobbi.project.coronamask.network.domain.StoreInfo

data class StoreResponse(
    @SerializedName("totalPages")
    val totalPages: Int,
    @SerializedName("totalCount")
    val totalCount: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("count")
    val count: Int,
    @SerializedName("storeInfos")
    val stores: List<StoreInfo>
)