package com.roy.ui

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
import com.applovin.mediation.ads.MaxAdView
import com.roy.R
import com.roy.databinding.AResultBinding
import com.roy.ext.createAdBanner
import com.roy.utils.displayToast
import com.roy.utils.saveBitmap

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: AResultBinding
    private val _binding get() = binding
    private var adView: MaxAdView? = null
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

        adView = this@ResultActivity.createAdBanner(
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
            startActivity(Intent(this, MainActivity::class.java))
            finish()
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

    @SuppressLint("SetTextI18n")
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
        backPreviousPage()
    }

    override fun onDestroy() {
        adView?.destroy()
        super.onDestroy()
    }

}
