package com.allergyguardian.allergyguardian.data.repository.market

import com.allergyguardian.allergyguardian.data.model.market.Market

interface MarketRepository {
    suspend fun getMarketData(name: String): List<Market>

}