package com.allergyguardian.allergyguardian.presentation.main

import android.os.Build
import android.os.Bundle
import android.view.WindowMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.allergyguardian.allergyguardian.R
import com.allergyguardian.allergyguardian.databinding.ActivityMainBinding
import com.allergyguardian.allergyguardian.presentation.franchise.FranchiseHomeFragment
import com.allergyguardian.allergyguardian.presentation.signin.SignInFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.AdSize
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeDriverService
import org.openqa.selenium.chrome.ChromeOptions

class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding:ActivityMainBinding

    private val adSize: AdSize
        get() {
            val displayMetrics = resources.displayMetrics
            val adWidthPixels =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val windowMetrics: WindowMetrics = this.windowManager.currentWindowMetrics
                    windowMetrics.bounds.width()
                } else {
                    displayMetrics.widthPixels
                }
            val density = displayMetrics.density
            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val backgroundScope = CoroutineScope(Dispatchers.IO)
//        backgroundScope.launch {
//            MobileAds.initialize(this@MainActivity) {}
//        }
//
//        setAd()
        val webDriverID = "webdriver.chrome.driver"
        val webDriverPath = "/C:/Users/jeong/AndroidStudioProjects/chromedriver-linux64/chromedriver-linux64/chromedriver"
        System.setProperty(webDriverID, webDriverPath)

        //엣지드라이버 옵션 설정하기
        val options = ChromeOptions()
        options.addArguments("--start-maximized")
        options.addArguments("--disable-popup-blocking")
        options.addArguments("--disable-default-apps")

        val service = ChromeDriverService.createDefaultService()
        //옵션이 적용된 엣지드라이버 불러오기
        val driver = ChromeDriver(service, options)

        try {
            //엣지드라이버로 기상청 메인화면(크롤링할 페이지) 띄우기(가져오기)
            driver.get("https://www.mega-mgccoffee.com/menu/?menu_category1=1&menu_category2=1")

            //현재 날씨부분만 가져오기
            val doc: WebElement = driver.findElement(
                By.xpath("/html/body/div[3]/div[3]/div/div[3]/div[2]/div[4]/div[1]/ul/div[1]/ul/li[1]/div/div[1]/div[1]/div[1]/b")
            )

            //가져온 현재 날씨를 콘솔로그에 출력
            println(doc.text)

            setFragment(FranchiseHomeFragment())

        } catch (e: Exception) {
        }
    }

    private fun setFragment(frag : Fragment) {
        supportFragmentManager.commit {
            add(R.id.main_frame, frag)
        }
    }

    private fun setAd() {
        val adView = AdView(this)
        adView.adUnitId = "ca-app-pub-3940256099942544/9214589741"
        adView.setAdSize(adSize)

        binding.adView.removeAllViews()
        binding.adView.addView(adView)

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

    }
    override fun onPause() {
        super.onPause()
        binding.adView.pause() // 액티비티가 일시 정지되면 광고도 일시 정지
    }
    override fun onResume() {
        super.onResume()
        binding.adView.resume() // 액티비티가 다시 시작되면 광고도 다시 시작
    }
    override fun onDestroy() {
        super.onDestroy()
        binding.adView.destroy() // 액티비티가 파괴되면 광고 리소스 해제
    }
}