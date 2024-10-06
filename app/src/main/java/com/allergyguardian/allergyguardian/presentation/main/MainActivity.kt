package com.allergyguardian.allergyguardian.presentation.main

import android.os.Build
import android.os.Bundle
import android.view.WindowMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.allergyguardian.allergyguardian.R
import com.allergyguardian.allergyguardian.databinding.ActivityMainBinding
import com.allergyguardian.allergyguardian.presentation.franchise.FranchiseCrawlerFragment
import com.allergyguardian.allergyguardian.presentation.franchise.franchise_menu.FranchiseMenuFragment
import com.allergyguardian.allergyguardian.presentation.signin.SignInFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.AdSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

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
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            MobileAds.initialize(this@MainActivity) {}
        }

        setAd()

        setFragment(FranchiseCrawlerFragment())

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