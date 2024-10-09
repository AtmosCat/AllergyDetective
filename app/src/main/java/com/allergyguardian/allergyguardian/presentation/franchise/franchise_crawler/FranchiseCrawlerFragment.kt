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
import com.allergyguardian.allergyguardian.presentation.FranchiseViewModel
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class FranchiseCrawlerFragment : Fragment() {

    private var _binding: FragmentFranchiseCrawlerBinding? = null

    private var instance = Menu()
    private val binding get() = _binding!!

    private var starbucksBevList: MutableList<Menu> = mutableListOf()
    private var twosomeList: MutableList<Menu> = mutableListOf()
    private var megacoffeeBevList: MutableList<Menu> = mutableListOf()

    private var imgurlList = mutableListOf<String>()
    private var nameList = mutableListOf<String>()
    private var allergyList = mutableListOf<String>()

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

//        starbucksBevCrawler(franchiseViewModel.starbucksBeverageUrls)
//        twosomeCrawler(franchiseViewModel.twosomeUrls)
        megacoffeeBevCrawler(franchiseViewModel.megacoffeeUrls, 4)
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
            starbucksBevList
            )
    }

    private fun twosomeCrawler(urls: List<String>) {
        val iterator = urls.iterator()
        loadNextUrl(
            iterator,
            "카페",
            "투썸플레이스",
            "document.querySelector('.swiper-slide img').src",
            "document.querySelector('.menu-detail-info-title dt').innerText",
            "document.querySelector('.section-menu-goup p').innerText",
            twosomeList
        )
    }

    private fun megacoffeeBevCrawler(urls: List<String>, totalPages: Int) {
        val iterator = (1..totalPages).iterator()
        loadNextUrl2(
            iterator,
            "카페",
            "메가커피",
            "Array.from(document.querySelectorAll('.cont_gallery_list_img img')).map(img => img.src)", // 선택자는 이상없음
            "Array.from(document.querySelectorAll('.inner_modal .cont_text_box .cont_text_title b')).map(b => b.innerText)", // 선택자 작동함
            "Array.from(document.querySelectorAll('.cont_text.cont_text_info')).map(el => el.innerText)",
            megacoffeeBevList,
            totalPages,
            "https://www.mega-mgccoffee.com/menu/?menu_category1=1&menu_category2=1"
        )
    }

    fun loadNextUrl(iterator: Iterator<String>, type: String, brand: String, imgSelector: String, nameSelector: String,
                    allergySelector: String, menuList: MutableList<Menu>) {
        if (iterator.hasNext()) {
            val url = iterator.next()
            instance = Menu(type = type, brand = brand, url = url)
            binding.webviewMenu.loadUrl(url)
            binding.webviewMenu.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    binding.webviewMenu.evaluateJavascript(imgSelector) { value ->
                        val imgurl = value.replace("\"", "")
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
                    menuList.add(instance)
                    // 크롤링이 끝나면 웹뷰를 닫고 다음 URL 로드
                    loadNextUrl(iterator, type, brand, imgSelector, nameSelector, allergySelector, menuList)
                }
            }
        }
    }

    fun loadNextUrl2(iterator: Iterator<Int>, type: String, brand: String, imgSelector: String, nameSelector: String,
                    allergySelector: String, menuList: MutableList<Menu>, totalPages: Int, url: String) {
        if (iterator.hasNext()) {
            val pageNumber  = iterator.next()
            binding.webviewMenu.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    binding.webviewMenu.evaluateJavascript("document.querySelector('.board_page_link[data-page=\"$pageNumber\"]').click();") { result ->
                        // 페이지 로드 후 데이터 추출
                        // 약간의 지연 후에 데이터 추출
                        binding.webviewMenu.postDelayed({
                            binding.webviewMenu.evaluateJavascript(imgSelector) { value ->
                                val listType = object : TypeToken<List<String>>() {}.type
                                val imageUrls: MutableList<String> =
                                    Gson().fromJson(value, listType)
                                imgurlList = imageUrls
                            }
                            binding.webviewMenu.evaluateJavascript(nameSelector) { value ->
                                val listType = object : TypeToken<List<String>>() {}.type
                                val names: MutableList<String> = Gson().fromJson(value, listType)
                                nameList = names
                            }
                            binding.webviewMenu.evaluateJavascript(allergySelector) { value ->
                                val listType = object : TypeToken<List<String>>() {}.type
                                var allergies: MutableList<String> =
                                    Gson().fromJson(value, listType)
                                allergies = allergies.filter { it.contains("알레르기") }.toMutableList()
                                val allergiesSize = allergies.size
                                for (i in 0..allergiesSize - 1) {
                                    allergies[i] = allergies[i].replace("\n", "")
                                    allergies[i] = allergies[i].replace("\t", "")
                                }
                                allergyList = allergies
                            }
                            for (i in 0..nameList.size - 1) {
                                menuList.add(
                                    Menu(
                                        imgurl = imgurlList[i],
                                        name = nameList[i],
                                        allergy = allergyList[i]
                                    )
                                )
                            }
                            loadNextUrl2(iterator, type, brand, imgSelector, nameSelector,
                                allergySelector, menuList, totalPages, url)
                        }, 1000)
                    }
                }
            }
            binding.webviewMenu.loadUrl(url)
        }
    }


}