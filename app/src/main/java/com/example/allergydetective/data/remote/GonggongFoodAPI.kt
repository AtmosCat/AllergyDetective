package com.example.allergydetective.data.remote

import com.example.allergydetective.data.model.food.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

// data.go.kr에서 데이터를 가져오는 API를 정의 -> GonggongFoodAPI
interface GonggongFoodAPI {
    // GET key -> 맨 앞 슬래시 제외하고 넣기
    @GET("getCertImgListServiceV3")
    suspend fun getGonggongFood(
        @Query("pageNo") pageNo: String,
        @Query("numOfRows") numOfRows: String,
        // returntype 안 맞을 수도 있으니까 json 명시해주기
        @Query("returnType") returnType: String = "json",
        // 디코딩 키(인코딩 키 X)
        @Query("ServiceKey") serviceKey: String = "YlgP80NwhNBwLETbohJrUha7ygmEO09Y35mxU/uyz0N90+HesPbErivbWW5/8bp6aNZxNP8HwPo9WokX3r6O8w=="
    ): ApiResponse

    @GET("getCertImgListServiceV3")
    suspend fun getFilteredApi(
        @Query("pageNo") pageNo: String,
        @Query("numOfRows") numOfRows: String,
        @Query("prdkind") prdkind: String,
        @Query("returnType") returnType: String = "json",
        @Query("ServiceKey") serviceKey: String = "YlgP80NwhNBwLETbohJrUha7ygmEO09Y35mxU/uyz0N90+HesPbErivbWW5/8bp6aNZxNP8HwPo9WokX3r6O8w=="
    ): ApiResponse
}