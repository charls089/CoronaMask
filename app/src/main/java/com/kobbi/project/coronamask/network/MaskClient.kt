package com.kobbi.project.coronamask.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MaskClient private constructor() {
    companion object {
        private const val BASE_URL = "https://8oi9s0nnth.apigw.ntruss.com"
        private var INSTANCE: MaskApi? = null

        @JvmStatic
        fun getInstance(): MaskApi {
            return INSTANCE ?: synchronized(this) {
                getClient().also {
                    INSTANCE = it
                }
            }
        }

        private fun getClient(): MaskApi {
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
            }.build().create(MaskApi::class.java)
        }
    }
}