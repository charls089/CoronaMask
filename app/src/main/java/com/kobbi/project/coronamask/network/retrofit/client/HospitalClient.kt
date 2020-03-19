package com.kobbi.project.coronamask.network.retrofit.client

import com.kobbi.project.coronamask.network.retrofit.api.HospitalApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class HospitalClient {
    companion object {
        private const val BASE_URL = "http://apis.data.go.kr"
        private var INSTANCE: HospitalApi? = null

        @JvmStatic
        fun getInstance(): HospitalApi {
            return INSTANCE
                ?: synchronized(this) {
                getClient()
                    .also {
                    INSTANCE = it
                }
            }
        }

        private fun getClient(): HospitalApi {
            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder().run {
                connectTimeout(30, TimeUnit.SECONDS)
                addInterceptor(interceptor)
                build()
            }

            return Retrofit.Builder().apply {
                baseUrl(BASE_URL)
                addConverterFactory(GsonConverterFactory.create())
                addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                client(client)
            }.build().create(HospitalApi::class.java)
        }
    }
}