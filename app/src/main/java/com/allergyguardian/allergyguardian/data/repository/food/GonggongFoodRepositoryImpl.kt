package com.allergyguardian.allergyguardian.data.repository.food

import com.allergyguardian.allergyguardian.data.model.food.Food
import com.allergyguardian.allergyguardian.network.food.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// data.go.kr에서 로데이터를 가져오는 함수를 Foodrepository의 getList() 함수를 받아와서 구현
// List<Food> 형태로 데이터를 받아옴

class GonggongFoodRepositoryImpl: FoodRepository {
    override suspend fun getList(): List<Food> {
        // GonggongFoodAPI 안에 구현해놓았던 getGonggongFood() 함수를 활용해서 데이터 받아옴
        val response = RetrofitClient.gonggongFoodAPI.getGonggongFood(numOfRows = "100", pageNo = "1")
        val items = response.body?.items?: emptyList()
        // 로데이터에서 필요한 파라미터들만 빼서 데이터 클래스 Food의 인자로 넣어서 Food 인스턴스 생성
        // List<Food>를 최종값으로 반환
        // mapNotNull: null이 아닌 것만 반환 / item.item이 null일 경우 let안의 Food 객체가 생성 X
        return items.mapNotNull { item ->
            item.item?.let {
                Food(
                    allergy = it.allergy.toString(),
                    imgurl1 = it.imgurl1.toString(),
                    imgurl2 = it.imgurl2.toString(),
                    manufacture = it.manufacture.toString(),
                    nutrient = it.nutrient.toString(),
                    prdkind = it.prdkind.toString(),
                    prdlstNm = it.prdlstNm.toString(),
                    rawmtrl = it.rawmtrl.toString(),
                    seller = it.seller.toString(),
                    prdlstReportNo = it.prdlstReportNo.toString(),
                    like = 0
                )
            }
        }
    }

    // 카테고리로 1차 거르고 나서 알러지로 거르는 건 그 다음에 하기
    override suspend fun getFilteredData(category: String): List<Food> {
        var pageNo = 1
        val numOfRows = 100
        val response =
            RetrofitClient.gonggongFoodAPI.getFilteredApi(
                pageNo = pageNo.toString(),
                numOfRows = numOfRows.toString(),
                prdkind = category
            )
        var items = response.body?.items
        return items!!.mapNotNull { item ->
            item.item?.let {
                Food(
                    allergy = it.allergy.toString(),
                    imgurl1 = it.imgurl1.toString(),
                    imgurl2 = it.imgurl2.toString(),
                    manufacture = it.manufacture.toString(),
                    nutrient = it.nutrient.toString(),
                    prdkind = it.prdkind.toString(),
                    prdlstNm = it.prdlstNm.toString(),
                    rawmtrl = it.rawmtrl.toString(),
                    seller = it.seller.toString(),
                    prdlstReportNo = it.prdlstReportNo.toString(),
                    like = 0
                )
            }
        }
    }

    // 카테고리로 1차 거르고 나서 알러지로 거르는 건 그 다음에 하기
    override suspend fun getMoreFilteredData(category: String): List<Food> {
        var pageNo = 1
        val numOfRows = 100
        val response =
            RetrofitClient.gonggongFoodAPI.getFilteredApi(
                pageNo = pageNo.toString(),
                numOfRows = numOfRows.toString(),
                prdkind = category
            )
        var items = response.body?.items
        return items!!.mapNotNull { item ->
            item.item?.let {
                Food(
                    allergy = it.allergy.toString(),
                    imgurl1 = it.imgurl1.toString(),
                    imgurl2 = it.imgurl2.toString(),
                    manufacture = it.manufacture.toString(),
                    nutrient = it.nutrient.toString(),
                    prdkind = it.prdkind.toString(),
                    prdlstNm = it.prdlstNm.toString(),
                    rawmtrl = it.rawmtrl.toString(),
                    seller = it.seller.toString(),
                    prdlstReportNo = it.prdlstReportNo.toString(),
                    like = 0
                )
            }
        }
    }

    override suspend fun getAllData(page: Int): List<Food> {
        var pageNo = page
        val numOfRows = 100
        val response =
            RetrofitClient.gonggongFoodAPI.getGonggongFood(
                pageNo = pageNo.toString(),
                numOfRows = numOfRows.toString()
            )
        var items = response.body?.items
        return items!!.mapNotNull { item ->
            item.item?.let {
                Food(
                    allergy = it.allergy.toString(),
                    imgurl1 = it.imgurl1.toString(),
                    imgurl2 = it.imgurl2.toString(),
                    manufacture = it.manufacture.toString(),
                    nutrient = it.nutrient.toString(),
                    prdkind = it.prdkind.toString(),
                    prdlstNm = it.prdlstNm.toString(),
                    rawmtrl = it.rawmtrl.toString(),
                    seller = it.seller.toString(),
                    prdlstReportNo = it.prdlstReportNo.toString(),
                    like = 0
                )
            }
        }
    }

    override suspend fun fetchAllData(): List<Food> {
        var allItems = listOf<Food>()
        var pageNo = 1
        val numOfRows = 100
        var hasMoreData = true

        while (hasMoreData) {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.gonggongFoodAPI.getGonggongFood(pageNo = pageNo.toString(), numOfRows = numOfRows.toString())
                }
                var items = response.body?.items
                if (items!!.isNotEmpty()) {
                    allItems.plus(items.mapNotNull { item ->
                        item.item?.let {
                            Food(
                                allergy = it.allergy.toString(),
                                imgurl1 = it.imgurl1.toString(),
                                imgurl2 = it.imgurl2.toString(),
                                manufacture = it.manufacture.toString(),
                                nutrient = it.nutrient.toString(),
                                prdkind = it.prdkind.toString(),
                                prdlstNm = it.prdlstNm.toString(),
                                rawmtrl = it.rawmtrl.toString(),
                                seller = it.seller.toString(),
                                prdlstReportNo = it.prdlstReportNo.toString(),
                                like = 0
                            )
                        }
                    })
                    pageNo++
                } else {
                    hasMoreData = false
                }
            } catch (e: Exception) {
                // 오류 처리
                e.printStackTrace()
                hasMoreData = false
            }
        }
        return allItems
    }
}

