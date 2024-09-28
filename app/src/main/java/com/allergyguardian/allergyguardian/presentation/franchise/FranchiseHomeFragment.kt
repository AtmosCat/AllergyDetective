package com.allergyguardian.allergyguardian.presentation.franchise

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeDriverService
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.edge.EdgeDriverService
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.support.ui.ExpectedConditions
import java.io.File
import java.time.Duration
import java.util.concurrent.TimeUnit

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
        CoroutineScope(Dispatchers.IO).launch {
            megaCoffeeBeveragesInfoCrawler()
        }
    }

    fun megaCoffeeBeveragesInfoCrawler() {
//        // 크롬드라이버 설정 경로 (크롬 드라이버 설치 경로 필요), chromedriver = 크롬 브라우저를 제어하는 WebDriver
////        System.setProperty("webdriver.edge.driver", "C:/Users/jeong/AndroidStudioProjects/edgedriver_win64/msedgedriver")
////        System.setProperty("webdriver.chrome.driver", "C:/Users/jeong/AndroidStudioProjects/chromedriver-linux64/chromedriver-linux64/chromedriver")
//
////        // 크롬 옵션 설정 (브라우저가 뜨지 않도록 설정)
////        val options = ChromeOptions()
////        options.addArguments("--headless") // 화면 없이 실행하려면 주석 해제
////        options.addArguments("--disable-gpu")
//
//        // WebDriver 초기화
////        val service = ChromeDriverService.Builder()
////            .usingPort(5228)
////            .build()
//        val driver: WebDriver = ChromeDriver()
////        val driver: WebDriver = EdgeDriver()
//        driver.manage().deleteAllCookies()
//        driver.manage().window().maximize()
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10))
//        try {
//            val url = "https://www.mega-mgccoffee.com/menu/?menu_category1=1&menu_category2=1"
//            driver.get(url) // 페이지 로드
//
//            // 암시적 대기
//            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS)
//
//            // 특정 모달이 나타날 때까지 대기
//            val wait = WebDriverWait(driver, Duration.ofSeconds(10))
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inner_modal")))
//
//            // 제목 크롤링
//            val titleElement: WebElement = driver.findElement(By.cssSelector(".cont_text_title b"))
//            val title = titleElement.text
//
//            // 정보 크롤링
//            val infoElement: WebElement = driver.findElement(By.cssSelector(".cont_text_info"))
//            val info = infoElement.text
//
//            // 제공량 크롤링
//            val servingSizeElement: WebElement = driver.findElement(By.xpath("//div[contains(text(), '20oz')]"))
//            val servingSize = servingSizeElement.text
//
//            // 칼로리 정보 크롤링
//            val calorieElement: WebElement = driver.findElement(By.xpath("//div[contains(text(), '1회 제공량')]"))
//            val calorieInfo = calorieElement.text
//
//            // 설명 크롤링
//            val descriptionElement: WebElement = driver.findElement(By.xpath("//div[contains(text(), '부드럽고 달콤한')]"))
//            val description = descriptionElement.text
//
//            // 알레르기 정보 크롤링
//            val allergyElement: WebElement = driver.findElement(By.xpath("//div[contains(text(), '알레르기 성분')]"))
//            val allergyInfo = allergyElement.text
//
//            // 영양 성분 크롤링
//            val nutritionList = driver.findElements(By.cssSelector(".cont_list li"))
//            val nutritionInfo = nutritionList.map { it.text }
//
//            // 출력
//            println("제목: $title")
//            println("정보: $info")
//            println("제공량: $servingSize")
//            println("칼로리 정보: $calorieInfo")
//            println("설명: $description")
//            println("알레르기 정보: $allergyInfo")
//            println("영양 성분: $nutritionInfo")
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        } finally {
//            // 브라우저 닫기
//            driver.quit()
//        }
    }
}