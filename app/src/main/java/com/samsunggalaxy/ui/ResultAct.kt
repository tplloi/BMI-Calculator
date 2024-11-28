package com.samsunggalaxy.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.applovin.mediation.ads.MaxInterstitialAd
import com.samsunggalaxy.R
import com.samsunggalaxy.databinding.AResultBinding
import com.samsunggalaxy.ext.createAdBanner
import com.samsunggalaxy.ext.displayToast
import com.samsunggalaxy.ext.saveBitmap
import java.util.concurrent.TimeUnit
import kotlin.math.min
import kotlin.math.pow

class ResultAct : AppCompatActivity() {
    private lateinit var binding: AResultBinding
    private val _binding get() = binding
    private var adView: MaxAdView? = null
    private var interstitialAd: MaxInterstitialAd? = null
    private var retryAttempt = 0
    private var weight: Double = 1.0
    private var height: Double = 1.0
    private var result: Double = 0.0
    private var gender: Int = 0

    // handle permission dialog
    private val requestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) shareImage() else showErrorDialog()
        }

    private fun showErrorDialog() {
        displayToast("Please allow External Storage Read and Write Permissions.")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.a_result)

        setupViews()
    }

    private fun setupViews() {
        createAdInter()

        weight = intent.getDoubleExtra("Weight", 50.0)
        height = intent.getDoubleExtra("Height", 1.0)
        gender = intent.getIntExtra("Gender", 0)

        bmiCal()
        animationView()

        _binding.cvReload.setOnClickListener {
            backPreviousPage()
        }
        _binding.ivDeleteBtn.setOnClickListener {
            backPreviousPage()
        }
        _binding.ivShare.setOnClickListener {
            shareImage()
        }

        adView = this@ResultAct.createAdBanner(
            logTag = javaClass.simpleName,
            bkgColor = Color.TRANSPARENT,
            viewGroup = binding.flAd,
            isAdaptiveBanner = true,
        )
    }

    private fun shareImage() {
        if (!isStoragePermissionGranted()) {
            requestLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return
        }

        // unHide the app logo and name
        val imageURI = _binding.llDetailView.drawToBitmap().let { bitmap ->
            saveBitmap(this, bitmap)
        } ?: run {
            displayToast("Error occurred!")
            return
        }

        val intent = ShareCompat.IntentBuilder(this)
            .setType("image/jpeg")
            .setStream(imageURI)
            .intent

        startActivity(Intent.createChooser(intent, null))
    }

    private fun isStoragePermissionGranted(): Boolean = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    private fun backPreviousPage() {
        animationViewUp()
        Handler(Looper.getMainLooper()).postDelayed({
//            startActivity(Intent(this, MainAct::class.java))
//            overridePendingTransition(0, 0)
            finish()
            overridePendingTransition(0, 0)
            showAd {}
        }, 600)
    }

    private fun animationView() {

        _binding.apply {

            ivDesk.translationY = 100f
            tvResult.translationY = 40f
            tvBmi.translationY = 50f
            tvBmiTextNormal.translationY = 50f
            ivDeleteBtn.translationY = 70f
            cvReload.translationY = 70f
            ivShare.translationY = 70f

            ivDesk.alpha = 0f
            tvResult.alpha = 0f
            tvBmi.alpha = 0f
            tvBmiTextNormal.alpha = 0f
            ivDeleteBtn.alpha = 0f
            cvReload.alpha = 0f
            ivShare.alpha = 0f

            ivDesk.setPadding(100)
            ivDesk.animate().translationY(0f).alpha(1f).setDuration(500).setStartDelay(300)
                .start()
            ivDesk.setPadding(0)
            tvResult.animate().translationY(0f).alpha(1f).setDuration(500).setStartDelay(500)
                .start()
            tvBmi.animate().translationY(0f).alpha(1f).setDuration(500).setStartDelay(450).start()
            tvBmiTextNormal.animate().translationY(0f).alpha(.3f).setDuration(500)
                .setStartDelay(500)
                .start()
            ivDeleteBtn.animate().translationY(0f).alpha(.3f).setDuration(500).setStartDelay(600)
                .start()
            cvReload.animate().translationY(0f).alpha(1f).setDuration(500).setStartDelay(750)
                .start()
            ivShare.animate().translationY(0f).alpha(.3f).setDuration(500).setStartDelay(900)
                .start()
        }
    }

    private fun animationViewUp() {

        _binding.apply {

            textView.animate().translationY(0f).alpha(0f).setDuration(500).setStartDelay(0)
                .start()
            ivDesk.animate().translationY(-250f).alpha(0f).setDuration(500).setStartDelay(0)
                .start()

            tvResult.animate().translationY(-250f).alpha(0f).setDuration(500).setStartDelay(50)
                .start()
            tvBmi.animate().translationY(-250f).alpha(0f).setDuration(500).setStartDelay(100)
                .start()
            tvBmiTextNormal.animate().translationY(-250f).alpha(0f).setDuration(500)
                .setStartDelay(150)
                .start()
            ivDeleteBtn.animate().translationY(-250f).alpha(0f).setDuration(300).setStartDelay(200)
                .start()
            cvReload.animate().translationY(-250f).alpha(0f).setDuration(300)
                .setStartDelay(250).start()
            ivShare.animate().translationY(-250f).alpha(0f).setDuration(300).setStartDelay(300)
                .start()
        }
    }

    private fun bmiCal() {
        if (height > 0 && weight > 0) {
            if (gender == 0) {
                bmiCalMale()
            } else if (gender == 1) {
                bmiCalFemale()
            }
            showResult()
        }

    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun showResult() {
        val solution = String.format("%.1f", result)
        _binding.tvResult.text = solution
        _binding.tvBmi.apply {
            if (result < 18.5) {
                this.text = "You are Under Weight"
            } else if (result >= 18.5 && result < 24.9) {
                this.text = "You are Healthy"
            } else if (result >= 24.9 && result < 30) {
                this.text = "You are Overweight"
            } else if (result >= 30) {
                this.text = "You are Suffering from Obesity"
            }
        }

    }

    private fun bmiCalMale() {
        result = ((weight / (height * height)) * 10000)
    }

    private fun bmiCalFemale() {
        result = ((weight / (height * height)) * 10000)
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        backPreviousPage()
    }

    override fun onDestroy() {
        adView?.destroy()
        super.onDestroy()
    }

    private fun createAdInter() {
        val enableAdInter = getString(R.string.EnableAdInter) == "true"
        if (enableAdInter) {
            interstitialAd = MaxInterstitialAd(getString(R.string.INTER), this)
            interstitialAd?.let { ad ->
                ad.setListener(object : MaxAdListener {
                    override fun onAdLoaded(p0: MaxAd) {
                        print("onAdLoaded")
                        retryAttempt = 0
                    }

                    override fun onAdDisplayed(p0: MaxAd) {
                        print("onAdDisplayed")
                    }

                    override fun onAdHidden(p0: MaxAd) {
                        print("onAdHidden")
                        // Interstitial Ad is hidden. Pre-load the next ad
                        interstitialAd?.loadAd()
                    }

                    override fun onAdClicked(p0: MaxAd) {
                        print("onAdClicked")
                    }

                    override fun onAdLoadFailed(p0: String, p1: MaxError) {
                        print("onAdLoadFailed")
                        retryAttempt++
                        val delayMillis =
                            TimeUnit.SECONDS.toMillis(2.0.pow(min(6, retryAttempt)).toLong())

                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                interstitialAd?.loadAd()
                            }, delayMillis
                        )
                    }

                    override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
                        print("onAdDisplayFailed")
                        // Interstitial ad failed to display. We recommend loading the next ad.
                        interstitialAd?.loadAd()
                    }

                })
                ad.setRevenueListener {
                    print("onAdDisplayed")
                }

                // Load the first ad.
                ad.loadAd()
            }
        }
    }

    private fun showAd(runnable: Runnable) {
        val enableAdInter = getString(R.string.EnableAdInter) == "true"
        if (enableAdInter) {
            interstitialAd?.let { ad ->
                if (ad.isReady) {
                    ad.showAd()
                    runnable.run()
                }
            }
        }
    }

}
