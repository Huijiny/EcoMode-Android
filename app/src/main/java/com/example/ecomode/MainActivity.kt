package com.example.ecomode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ecomode.databinding.FragmentMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}