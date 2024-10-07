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
//    data class Menu(
//        val id : String = "",
//        val type: String = "", // 카페, 패스트푸드, 베이커리/도넛...
//        val brand: String = "", // 브랜드
//        val name: String = "", // 메뉴명
//        val allergy: String = "", // 알러지유발성분
//        val weight: String = "", // 중량
//        val kcal: String = "", // 열량
//        val natrium: String = "", // 나트륨
//        val sugar: String = "", // 당류
//        val fat: String = "", // 지방
//        val protein: String = "", // 단백질
//        val caffeine: String = "", // 카페인
//        val nutrients: String = "", // 영양성분
//        val img: String = "" // 이미지
//    )

    private fun starbucksBevCrawler(urls: List<String>) {
        val iterator = urls.iterator() // URL 리스트의 Iterator 생성
        loadNextUrl(iterator) // 첫 번째 URL 로드
    }
        fun loadNextUrl(iterator: Iterator<String>) {
            if (iterator.hasNext()) {
                val url = iterator.next()
                instance = Menu(type = "카페", brand = "스타벅스")
                binding.webviewMenu.loadUrl(url)
                binding.webviewMenu.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView, url: String) {
                        super.onPageFinished(view, url)

                        binding.webviewMenu.evaluateJavascript("document.querySelector('.zoomImg').src") { value ->
                            val imgurl = value.replace("\"", "") // 쌍따옴표 제거
                            instance.imgurl = imgurl
                        }
                        binding.webviewMenu.evaluateJavascript("document.querySelector('.product_view_detail h4').innerText") { value ->
                            val name = value.replace("\"", "")
                            instance.name = name
                        }
                        binding.webviewMenu.evaluateJavascript("document.querySelector('.product_factor > p').innerText") { value ->
                            var allergy = value.replace("\"", "")
                            allergy = allergy.replace("알레르기 유발요인: ", "")
                            instance.allergy = allergy
                        }
                        binding.webviewMenu.evaluateJavascript("document.querySelector('.selectTxt2 p b').innerText") { value ->
                            val weight = value.replace("\"", "")
                            instance.weight = weight
                        }
                        binding.webviewMenu.evaluateJavascript("document.querySelector('.product_info_content .kcal dd').innerText") { value ->
                            val kcal = value.replace("\"", "")
                            instance.kcal = kcal
                        }
                        binding.webviewMenu.evaluateJavascript("document.querySelector('.product_info_content .sat_FAT dd').innerText") { value ->
                            val fat = value.replace("\"", "")
                            instance.fat = fat
                        }
                        binding.webviewMenu.evaluateJavascript("document.querySelector('.product_info_content .protein dd').innerText") { value ->
                            val protein = value.replace("\"", "")
                            instance.protein = protein
                        }
                        binding.webviewMenu.evaluateJavascript("document.querySelector('.product_info_content .sodium dd').innerText") { value ->
                            val natrium = value.replace("\"", "")
                            instance.natrium = natrium
                        }
                        binding.webviewMenu.evaluateJavascript("document.querySelector('.product_info_content .sugars dd').innerText") { value ->
                            val sugar = value.replace("\"", "")
                            instance.sugar = sugar
                        }
                        binding.webviewMenu.evaluateJavascript("document.querySelector('.caffeine last dl dd').innerText") { value ->
                            val caffeine = value.replace("\"", "")
                            instance.caffeine = caffeine
                        }

                        starbucksBevList.add(instance)

                        // 크롤링이 끝나면 웹뷰를 닫고 다음 URL 로드
                        loadNextUrl(iterator)
                    }
                }
            }
        }


}