package com.samsunggalaxy

import android.app.Application
import com.google.android.material.color.DynamicColors

//TODO firebase
//TODO applovin ad
//TODO keep value cuoi cung de hien thi len wheel view

//TODO rate app
//TODO roy93~ review in app
//TODO more app
//TODO share app
//TODO policy
//TODO leak canary
//TODO keystore
//TODO splash screen

//done mckimquyen
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
