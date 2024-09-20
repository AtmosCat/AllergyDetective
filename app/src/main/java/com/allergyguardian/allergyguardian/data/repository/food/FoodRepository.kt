package com.allergyguardian.allergyguardian.data.repository.food

import com.allergyguardian.allergyguardian.data.model.food.Food

// Food 로데이터를 List<Food> 형태로 받아오는 getList() 함수를 통합 인터페이스 FoodRepository에서 정의
// 구현부는 RepositoryImpl에서!
interface FoodRepository {
    suspend fun getList(): List<Food>

    suspend fun getFilteredData(category: String): List<Food>

    suspend fun getMoreFilteredData(category: String): List<Food>

    suspend fun getAllData(page: Int): List<Food>

    suspend fun fetchAllData(): List<Food>

}