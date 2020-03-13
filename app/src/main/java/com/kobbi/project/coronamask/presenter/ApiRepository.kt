package com.kobbi.project.coronamask.presenter

import com.kobbi.project.coronamask.network.MaskClient
import com.kobbi.project.coronamask.network.response.StoreDetailResponse
import io.reactivex.Observable

class ApiRepository {

    fun requestStoreDetails(
        lat: Double,
        lng: Double,
        boundary: Int
    ): Observable<StoreDetailResponse> {
        val params = HashMap<String, Any>().apply {
            put("lat", lat)
            put("lng", lng)
            put("m", boundary)
        }
        return MaskClient.getInstance().requestStoreDetailInfo(params)
    }
}