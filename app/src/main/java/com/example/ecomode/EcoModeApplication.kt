package com.example.ecomode

import android.app.Application

class EcoModeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeSharedPreferences()
    }

    fun initializeSharedPreferences() {

    }
}