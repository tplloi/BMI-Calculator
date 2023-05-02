package com.roy

import android.app.Application
import com.google.android.material.color.DynamicColors

//TODO ad
//TODO firebase

//TODO keystore
//TODO keep value cuoi cung de hien thi len wheel view

//done
//ic launcher
//rate app
//more app
//share app
//policy
//ad id manifest
//leak canary
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
