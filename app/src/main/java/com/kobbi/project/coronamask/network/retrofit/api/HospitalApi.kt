package com.kobbi.project.coronamask.network.retrofit.api

import com.kobbi.project.coronamask.network.retrofit.response.HospitalResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface HospitalApi {
    @GET("/B551182/pubReliefHospService/getpubReliefHospList")
    fun requestHospital(
        @QueryMap params: @JvmSuppressWildcards Map<String, Any>
    ): Observable<HospitalResponse>

}