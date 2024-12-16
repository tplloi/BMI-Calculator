package com.samsunggalaxy

import android.app.Application
import com.google.android.material.color.DynamicColors

//TODO firebase
//TODO applovin ad
//TODO keep value cuoi cung de hien thi len wheel view
//TODO splash screen

//done mckimquyen
//review in app bingo
//keystore
//leak canary
//policy
//share app
//rate app
//more app
//ic launcher
//change color
//ad id manifest
//scale 1.0
//120hz

class GalaxyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
