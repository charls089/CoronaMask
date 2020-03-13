package com.kobbi.project.coronamask.network

import com.kobbi.project.coronamask.network.response.StoreDetailResponse
import com.kobbi.project.coronamask.network.response.StoreResponse
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface MaskApi {
    @GET("/stores/json")
    fun requestStoreInfo(
        @QueryMap params: @JvmSuppressWildcards Map<String,Any>
    ): Call<StoreResponse>

    @GET("/sales/json")
    fun requestSalesInfo(
        @QueryMap params: @JvmSuppressWildcards Map<String,Any>
    ): Call<StoreResponse>

    @GET("/corona19-masks/v1/storesByGeo/json")
    fun requestStoreDetailInfo(
        @QueryMap params: @JvmSuppressWildcards Map<String,Any>
    ): Observable<StoreDetailResponse>
}