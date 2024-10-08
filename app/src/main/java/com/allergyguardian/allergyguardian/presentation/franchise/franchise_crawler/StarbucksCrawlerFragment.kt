package com.allergyguardian.allergyguardian.presentation.franchise.franchise_crawler

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
import com.allergyguardian.allergyguardian.data.model.franchise.Menu
import com.allergyguardian.allergyguardian.databinding.FragmentFranchiseCrawlerBinding
import com.allergyguardian.allergyguardian.databinding.FragmentStarbucksCrawlerBinding
import com.allergyguardian.allergyguardian.presentation.FranchiseViewModel

class StarbucksCrawlerFragment : Fragment() {

    private var _binding: FragmentStarbucksCrawlerBinding? = null

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
        _binding = FragmentStarbucksCrawlerBinding.inflate(inflater, container, false)
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
            "document.querySelector('.caffeine last dl dd').innerText"
        )
    }

    fun loadNextUrl(iterator: Iterator<String>, type: String, brand: String, imgSelector: String, nameSelector: String,
                    allergySelector: String, weightSelector: String, kcalSelector: String, fatSelector: String,
                    proteinSelector: String, natriumSelector: String, sugarSelector: String, caffeineSelector: String) {
        if (iterator.hasNext()) {
            val url = iterator.next()
            instance = Menu(type = type, brand = brand, hotice = "메뉴명 참고", url = url)
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
                    starbucksBevList.add(instance)
                    // 크롤링이 끝나면 웹뷰를 닫고 다음 URL 로드
                    loadNextUrl(iterator, type, brand, imgSelector, nameSelector,
                        allergySelector, weightSelector, kcalSelector, fatSelector,
                        proteinSelector, natriumSelector, sugarSelector, caffeineSelector)
                }
            }
        }
    }


}