package com.example.allergydetective.data.remote

import com.example.allergydetective.data.model.market.MarketData
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MarketAPI {
    // GET key -> 맨 앞 슬래시 제외하고 넣기
    @GET("search/shop.json")
    suspend fun getMarketData(
        @Header("X-Naver-Client-Id") clientID: String = "UXZX2p65yR1sddMIvFax",
        @Header("X-Naver-Client-Secret") clientSecret: String = "hCZNXODHEH",
        @Query("query") query: String,
        @Query("display") display: Int = 5,
        @Query("sort") sort: String = "sim"
    ): MarketData
}