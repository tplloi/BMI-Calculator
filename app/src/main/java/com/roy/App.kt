package com.roy

import android.app.Application
import com.google.android.material.color.DynamicColors

//TODO ad
//TODO ad id manifest
//TODO firebase
//TODO keystore
//TODO leak canary
//TODO keep value cuoi cung de hien thi len wheel view

//done
//ic launcher
//rate app
//more app
//share app
//policy
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
