package com.allergyguardian.allergyguardian.network.market

import com.allergyguardian.allergyguardian.data.remote.MarketAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientMarket {
    private const val BASE_URL = "https://openapi.naver.com/v1/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }).build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val marketAPI: MarketAPI = retrofit.create(MarketAPI::class.java)

}
