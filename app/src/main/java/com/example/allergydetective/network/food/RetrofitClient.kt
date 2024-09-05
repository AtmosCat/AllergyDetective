package com.example.allergydetective.network.food

import com.example.allergydetective.data.remote.GonggongFoodAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // BaseURL은 공공데이터 홈페이지에 있는 EndP Point 그대로 넣기
    // 맨뒤에 슬래시 추가해서 API의 GET이랑 연결되도록
    // 헷갈리면 홈페이지에서 Test 돌려보고 양식 그대로 맞추기
    private const val BASE_URL = "https://apis.data.go.kr/B553748/CertImgListServiceV3/"

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

    val gonggongFoodAPI: GonggongFoodAPI = retrofit.create(GonggongFoodAPI::class.java)
}
