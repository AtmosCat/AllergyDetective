package com.allergyguardian.allergyguardian.presentation.franchise.franchise_menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.recyclerview.widget.LinearLayoutManager
import com.allergyguardian.allergyguardian.R
import com.allergyguardian.allergyguardian.databinding.FragmentFranchiseHomeBinding
import com.allergyguardian.allergyguardian.databinding.FragmentFranchiseMenuBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FranchiseMenuFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentFranchiseMenuBinding? = null

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FranchiseMenuFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFranchiseMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webviewMenu.settings.javaScriptEnabled = true

        /* 웹뷰에서 새 창이 뜨지 않도록 방지하는 구문 */
        binding.webviewMenu.webViewClient = WebViewClient()
        binding.webviewMenu.webChromeClient = WebChromeClient()

        /* 링크 주소를 로드 */
        binding.webviewMenu.loadUrl("https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000002487")

        binding.webviewMenu.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                extractDataFromWebView()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.webviewMenu.destroy()
    }

    private fun extractDataFromWebView() {
        // JavaScript를 사용하여 특정 데이터를 추출
        binding.webviewMenu.evaluateJavascript("document.querySelector('.product_factor > p').innerText") { value ->
            val imgurl = value
            val extractedString = value.replace("\"", "") // 쌍따옴표 제거
            saveData(extractedString)
        }
    }

    private fun saveData(data: String) {

    }
}
