package com.example.ecomode.main

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.example.ecomode.data.sharedpreferences.UserSharedPreferences
import com.example.ecomode.data.sharedpreferences.UserSharedPreferencesImpl
import com.example.ecomode.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var ecoModeSharedPreferences: UserSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ecoModeSharedPreferences = UserSharedPreferencesImpl.create(this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

}