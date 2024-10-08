package com.allergyguardian.allergyguardian.presentation.franchise.franchise_crawler

import java.lang.Exception

fun main(args: Array<String>) {
    //프로퍼티 설정하기
//    val webDriverID = "webdriver.chrome.driver"
//    val webDriverPath = "/C:/Users/jeong/AndroidStudioProjects/chromedriver-linux64/chromedriver-linux64/chromedriver"
//    System.setProperty(webDriverID, webDriverPath)
//
//    //엣지드라이버 옵션 설정하기
//    val options = ChromeOptions()
//    options.addArguments("--start-maximized")
//    options.addArguments("--disable-popup-blocking")
//    options.addArguments("--disable-default-apps")
//
//    //옵션이 적용된 엣지드라이버 불러오기
//    val driver = ChromeDriver(options)
//
//    try {
//        //엣지드라이버로 기상청 메인화면(크롤링할 페이지) 띄우기(가져오기)
//        driver.get("https://www.mega-mgccoffee.com/menu/?menu_category1=1&menu_category2=1")
//
//        //현재 날씨부분만 가져오기
//        val doc: WebElement = driver.findElement(
//            By.xpath("/html/body/div[3]/div[3]/div/div[3]/div[2]/div[4]/div[1]/ul/div[1]/ul/li[1]/div/div[1]/div[1]/div[1]/b")
//        )
//
//        //가져온 현재 날씨를 콘솔로그에 출력
//        println(doc.text)
//    } catch (e: Exception) {
//        e.printStackTrace()
//    } finally {
//        //사용이 끝난 엣지드라이버 종
//        driver.close()
//    }
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