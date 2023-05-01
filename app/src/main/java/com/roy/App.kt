package com.roy

import android.app.Application
import com.google.android.material.color.DynamicColors

//TODO ic launcher
//TODO rate app
//TODO more app
//TODO share app
//TODO ad
//TODO policy
//TODO ad id manifest
//TODO firebase
//TODO keystore
//TODO leak canary
//TODO keep value cuoi cung de hien thi len wheel view
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
