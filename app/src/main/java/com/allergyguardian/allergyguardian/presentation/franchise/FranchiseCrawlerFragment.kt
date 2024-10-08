package com.allergyguardian.allergyguardian.presentation.franchise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.allergyguardian.allergyguardian.R
import com.allergyguardian.allergyguardian.data.model.franchise.Menu
import com.allergyguardian.allergyguardian.databinding.FragmentFranchiseCrawlerBinding
import com.allergyguardian.allergyguardian.databinding.FragmentFranchiseMenuBinding
import com.allergyguardian.allergyguardian.presentation.FranchiseViewModel
import com.allergyguardian.allergyguardian.presentation.PostViewModel
import java.util.UUID

class FranchiseCrawlerFragment : Fragment() {

    private var _binding: FragmentFranchiseCrawlerBinding? = null

    private var instance = Menu()
    private val binding get() = _binding!!

    private var starbucksBevList: MutableList<Menu> = mutableListOf()

    private val franchiseViewModel: FranchiseViewModel by activityViewModels {
        viewModelFactory { initializer { FranchiseViewModel(requireActivity().application) } }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFranchiseCrawlerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webviewMenu.settings.javaScriptEnabled = true

        /* 웹뷰에서 새 창이 뜨지 않도록 방지하는 구문 */
        binding.webviewMenu.webViewClient = WebViewClient()
        binding.webviewMenu.webChromeClient = WebChromeClient()

        starbucksBevCrawler(franchiseViewModel.starbucksBeverageUrls)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.webviewMenu.destroy()
    }

    private fun starbucksBevCrawler(urls: List<String>) {
        val iterator = urls.iterator() // URL 리스트의 Iterator 생성
        loadNextUrl(
            iterator,
            "카페",
            "스타벅스",
            "document.querySelector('.zoomImg').src",
            "document.querySelector('.product_view_detail h4').innerText",
            "document.querySelector('.product_factor > p').innerText",
            "document.querySelector('.selectTxt2 p b').innerText",
            "document.querySelector('.product_info_content .kcal dd').innerText",
            "document.querySelector('.product_info_content .sat_FAT dd').innerText",
            "document.querySelector('.product_info_content .protein dd).innerText",
            "document.querySelector('.product_info_content .sodium dd').innerText",
            "document.querySelector('.product_info_content .sugars dd').innerText",
            "document.querySelector('.caffeine last dl dd').innerText",
            ""
            )
    }

    private fun twosomeHotBevCrawler(urls: List<String>) {
        val iterator = urls.iterator()
        loadNextUrl(
            iterator,
            "카페",
            "투썸플레이스",
            "document.querySelector('.swiper-slide').src",
            "document.querySelector('.menu-detail-info-title dt').innerText",
            "document.querySelector('.section-menu-goup .desc01 mt-10').innerText",
            "document.querySelector('.selectTxt2 p b').innerText",
            "document.querySelector('.product_info_content .kcal dd').innerText",
            "document.querySelector('.product_info_content .sat_FAT dd').innerText",
            "document.querySelector('.product_info_content .protein dd).innerText",
            "document.querySelector('.product_info_content .sodium dd').innerText",
            "document.querySelector('.product_info_content .sugars dd').innerText",
            "document.querySelector('.caffeine last dl dd').innerText",
            "" // hot, ice 여부에 따라 class의 active 여부가 바뀜 -> 어쩌지
        )
    }

    // 지피티가 알려준 핫/아이스 구분하여 크롤링하는 방법. 시도 필요
//    private fun loadNextUrl(iterator: Iterator<String>, type: String, brand: String, imgSelector: String, nameSelector: String,
//                            allergySelector: String, weightSelector: String, kcalSelector: String, fatSelector: String,
//                            proteinSelector: String, natriumSelector: String, sugarSelector: String, caffeineSelector: String,
//                            hoticeSelector: String) {
//        if (iterator.hasNext()) {
//            val url = iterator.next()
//            instance = Menu(type = type, brand = brand, hotice = "메뉴명 참고")
//            binding.webviewMenu.loadUrl(url)
//
//            binding.webviewMenu.webViewClient = object : WebViewClient() {
//                override fun onPageFinished(view: WebView, url: String) {
//                    super.onPageFinished(view, url)
//
//                    // 탭 상태 확인
//                    binding.webviewMenu.evaluateJavascript("document.querySelector('li.is-active a').innerText") { tabStatus ->
//                        if (tabStatus.replace("\"", "") == "핫") {
//                            // 핫 탭이 활성화된 경우 정보 크롤링
//                            crawlHotInformation()
//                        } else {
//                            // 아이스 탭으로 전환 후 정보 크롤링
//                            binding.webviewMenu.evaluateJavascript("document.querySelector('li.tab a[data-code=\"010I\"]').click()") {
//                                // 클릭 후 아이스 정보 크롤링
//                                crawlIceInformation()
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private fun crawlHotInformation() {
//        // 핫 정보 크롤링 코드
//        binding.webviewMenu.evaluateJavascript("document.querySelector('.menu-detail-dl-wrap dd').innerText") { value ->
//            val servingSize = value.replace("\"", "")
//            instance.weight = servingSize // 325ml 크롤링
//            // 추가 정보 크롤링
//        }
//    }
//
//    private fun crawlIceInformation() {
//        // 아이스 정보 크롤링 코드
//        binding.webviewMenu.evaluateJavascript("document.querySelector('.menu-detail-dl-wrap dd').innerText") { value ->
//            val servingSize = value.replace("\"", "")
//            instance.weight = servingSize // 아이스 정보 크롤링
//            // 추가 정보 크롤링
//        }
//    }


//    data class Menu(
//        val id : String = UUID.randomUUID().toString(),
//        var type: String = "", // 카페, 패스트푸드, 베이커리/도넛...
//        var brand: String = "", // 브랜드
//        var name: String = "", // 메뉴명
//        var allergy: String = "", // 알러지유발성분
//        var weight: String = "", // 중량
//        var kcal: String = "", // 열량
//        var natrium: String = "", // 나트륨
//        var sugar: String = "", // 당류
//        var fat: String = "", // 지방
//        var protein: String = "", // 단백질
//        var caffeine: String = "", // 카페인
//        var nutrients: String = "", // 영양성분
//        var imgurl: String = "", // 이미지
//        val hotice: String = ""
//    )

    fun loadNextUrl(iterator: Iterator<String>, type: String, brand: String, imgSelector: String, nameSelector: String,
                    allergySelector: String, weightSelector: String, kcalSelector: String, fatSelector: String,
                    proteinSelector: String, natriumSelector: String, sugarSelector: String, caffeineSelector: String,
                    hoticeSelector: String) {
        if (iterator.hasNext()) {
            val url = iterator.next()
            instance = Menu(type = type, brand = brand, hotice = "메뉴명 참고")
            // 이름에 아이스 들어가있으면 ice로 인식하게? 예외사항 있는지 보기
            binding.webviewMenu.loadUrl(url)
            binding.webviewMenu.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    binding.webviewMenu.evaluateJavascript(imgSelector) { value ->
                        val imgurl = value.replace("\"", "") // 쌍따옴표 제거
                        instance.imgurl = imgurl
                    }
                    binding.webviewMenu.evaluateJavascript(nameSelector) { value ->
                        val name = value.replace("\"", "")
                        instance.name = name
                    }
                    binding.webviewMenu.evaluateJavascript(allergySelector) { value ->
                        var allergy = value.replace("\"", "")
                        instance.allergy = allergy
                    }
                    binding.webviewMenu.evaluateJavascript(weightSelector) { value ->
                        val weight = value.replace("\"", "")
                        instance.weight = weight
                    }
                    binding.webviewMenu.evaluateJavascript(kcalSelector) { value ->
                        val kcal = value.replace("\"", "")
                        instance.kcal = kcal
                    }
                    binding.webviewMenu.evaluateJavascript(fatSelector) { value ->
                        val fat = value.replace("\"", "")
                        instance.fat = fat
                    }
                    binding.webviewMenu.evaluateJavascript(proteinSelector) { value ->
                        val protein = value.replace("\"", "")
                        instance.protein = protein
                    }
                    binding.webviewMenu.evaluateJavascript(natriumSelector) { value ->
                        val natrium = value.replace("\"", "")
                        instance.natrium = natrium
                    }
                    binding.webviewMenu.evaluateJavascript(sugarSelector) { value ->
                        val sugar = value.replace("\"", "")
                        instance.sugar = sugar
                    }
                    binding.webviewMenu.evaluateJavascript(caffeineSelector) { value ->
                        val caffeine = value.replace("\"", "")
                        instance.caffeine = caffeine
                    }
                    binding.webviewMenu.evaluateJavascript(hoticeSelector) { value ->
                        val hotice = value.replace("\"", "")
                        instance.hotice = hotice
                    }
                    starbucksBevList.add(instance)
                    // 크롤링이 끝나면 웹뷰를 닫고 다음 URL 로드
                    loadNextUrl(iterator, type, brand, imgSelector, nameSelector,
                    allergySelector, weightSelector, kcalSelector, fatSelector,
                    proteinSelector, natriumSelector, sugarSelector, caffeineSelector, hoticeSelector)
                }
            }
        }
    }


}