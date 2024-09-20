package com.example.allergydetective.data.repository.market

import com.example.allergydetective.data.model.market.Market
import com.example.allergydetective.network.market.RetrofitClientMarket


class MarketRepositoryImpl: MarketRepository {
    override suspend fun getMarketData(name: String): List<Market> {
        val response = RetrofitClientMarket.marketAPI.getMarketData(query = name)
        val items = response.items?: emptyList()
        var size = items.size
        var marketItems: List<Market> = emptyList()

        try {
            for (i in 0 until size) {
                val marketItem = Market(
                    brand = items[i]?.brand.orEmpty(),
                    category1 = items[i]?.category1.orEmpty(),
                    category2 = items[i]?.category2.orEmpty(),
                    image = items[i]?.image.orEmpty(),
                    link = items[i]?.link.orEmpty(),
                    lprice = items[i]?.lprice.toString(),
                    maker = items[i]?.maker.orEmpty(),
                    mallName = items[i]?.mallName.orEmpty(),
                    title = items[i]?.title.orEmpty()
                )
                marketItems += marketItem
            }
            return marketItems

        } catch (e: Exception) {
            return marketItems  // 예외 발생 시 빈 리스트 반환
        }
    }
}


