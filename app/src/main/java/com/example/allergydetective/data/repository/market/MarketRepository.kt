package com.example.allergydetective.data.repository.market

import com.example.allergydetective.data.model.food.Food
import com.example.allergydetective.data.model.market.Market

interface MarketRepository {
    suspend fun getMarketData(name: String): List<Market>

}