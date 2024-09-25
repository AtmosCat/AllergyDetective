plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-android")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.allergyguardian.allergyguardian"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.allergyguardian.allergyguardian"
        minSdk = 31
        targetSdk = 34
        versionCode = 2
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.benchmark.common)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage.ktx)
    testImplementation(libs.junit)
    implementation(libs.coil)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.powerspinner)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics) // 파이어베이스 앱 분석
    implementation(libs.firebase.auth.ktx) // 파이어베이스 인증
    implementation(libs.play.services.auth)
    implementation (libs.firebase.ui.auth) // 파이어베이스 인증
    implementation(libs.google.firebase.firestore.ktx)
    implementation("com.google.android.gms:play-services-ads:23.3.0") // 애드몹
    implementation("org.jsoup:jsoup:1.18.1") // 크롤링
}