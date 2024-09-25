package com.allergyguardian.allergyguardian.presentation.franchise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.allergyguardian.allergyguardian.data.model.franchise.CoffeeBeverage
import org.jsoup.Jsoup
import com.allergyguardian.allergyguardian.databinding.FragmentFranchiseHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FranchiseHomeFragment : Fragment() {

    private var megaCoffeeBeverages = mutableListOf<CoffeeBeverage>()
    private var _binding: FragmentFranchiseHomeBinding? = null

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFranchiseHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        megaCoffeeBaveragesInfoCrawler()
    }
    data class CoffeeBeverage (
        val brand: String = "",
        val type: String = "",
        val name: String = "",
        val allergy: String = "",
        val weight: String = "",
        val kcal: String = "",
        val natrium: String = "",
        val sugar: String = "",
        val fat: String = "",
        val protein: String = "",
        val caffeine: String = "",
        val nutrients: String = "",
        val img: String = ""
    )

    fun megaCoffeeBaveragesInfoCrawler() {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
//            try {
                val urls = listOf(
                    "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000002487"
                )

                for (url in urls) {
                    val document = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .timeout(10000) // 타임아웃 시간을 10초로 설정
                        .followRedirects(true) // 리디렉션을 따라감
                        .get()

                    val elements = document.select("ul#menu_list > li") // 각 <li> 요소 선택

                    for (element in elements) {
                        val brand = "메가커피"
                        val type = "음료"

                        // 이름과 알레르기 성분 추출
                        val name = element.select(".cont_text_title b").text() // 제목
                        val allergy = element.select(".cont_text_info").text()
                            .replace("알레르기 성분 : ", "") // 알레르기 성분 텍스트 제거

                        // 20oz와 kcal 추출
                        val weight =
                            element.select(".inner_modal .cont_text_inner:contains(oz)").text()
                        val kcal =
                            element.select(".inner_modal .cont_text_inner:contains(kcal)").text()

                        // 영양소 리스트 추출 (리스트가 비어 있을 경우 처리)
                        val nutrients = element.select(".cont_list2 ul li").map { it.text() }
                        val fat = nutrients.getOrNull(0) ?: "정보 없음"
                        val sugar = nutrients.getOrNull(1) ?: "정보 없음"
                        val natrium = nutrients.getOrNull(2) ?: "정보 없음"
                        val protein = nutrients.getOrNull(3) ?: "정보 없음"
                        val caffeine = nutrients.getOrNull(4) ?: "정보 없음"

                        // 이미지 URL 추출
                        val img = element.select(".cont_gallery_list_img img").attr("src")

                        val megaCoffeeBeverage = CoffeeBeverage(
                            brand = brand,
                            type = type,
                            name = name,
                            allergy = allergy,
                            weight = weight,
                            kcal = kcal,
                            natrium = natrium,
                            sugar = sugar,
                            fat = fat,
                            protein = protein,
                            caffeine = caffeine,
                            img = img
                        )
                        megaCoffeeBeverages.add(megaCoffeeBeverage)
                    }
                }
                println(megaCoffeeBeverages)
                // 데이터 DB 저장

//            } catch (e: Exception) {
//                println("크롤링 중 오류 발생: ${e.message}")
//            }
        }
    }
}