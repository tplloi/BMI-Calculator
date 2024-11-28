package com.samsunggalaxy

import android.app.Application
import com.google.android.material.color.DynamicColors

//TODO firebase
//TODO keep value cuoi cung de hien thi len wheel view
//TODO change color
//TODO ic launcher
//TODO rate app
//TODO more app
//TODO share app
//TODO policy
//TODO leak canary
//TODO keystore
//TODO applovin ad

//done mckimquyen
//ad id manifest
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
