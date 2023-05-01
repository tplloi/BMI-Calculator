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
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
