package com.kobbi.project.coronamask.presenter

import android.content.Context
import com.kobbi.project.coronamask.model.HospitalType
import com.kobbi.project.coronamask.network.retrofit.client.HospitalClient
import com.kobbi.project.coronamask.network.retrofit.client.MaskClient
import com.kobbi.project.coronamask.network.retrofit.response.HospitalResponse
import com.kobbi.project.coronamask.network.retrofit.response.StoreDetailResponse
import io.reactivex.Observable
import java.util.*
import kotlin.collections.HashMap

object ApiRepository {
    @JvmStatic
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

    @JvmStatic
    fun requestHospital(context: Context, type: HospitalType): Observable<HospitalResponse> {
        val params = HashMap<String, Any>().apply {
            put("ServiceKey", getApiKey(context))
            put("numOfRows", 1000)
            put("pageNo", 1)
            put("spclAdmTyCd", type.code)
        }
        return HospitalClient.getInstance().requestHospital(params)
    }

    private fun getApiKey(context: Context): String {
        val asset = context.applicationContext.assets.open("apiConfig.properties")
        return Properties().run {
            load(asset)
            getProperty("data_api_key")
        }
    }
}