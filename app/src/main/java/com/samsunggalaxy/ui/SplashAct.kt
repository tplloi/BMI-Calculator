package com.samsunggalaxy.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.samsunggalaxy.BaseActivity
import kotlin.jvm.java

@SuppressLint("CustomSplashScreen")
class SplashAct : BaseActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        val intent = Intent(this, MainAct::class.java)
        startActivity(intent)
        finish()
    }
}
