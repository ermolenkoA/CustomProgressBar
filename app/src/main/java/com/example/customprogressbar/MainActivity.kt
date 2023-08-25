package com.example.customprogressbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customprogressbar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.submitButton.setOnClickListener {
            onButtonClicked()
        }
    }

    private fun onButtonClicked() {
        with(binding) {
            val value = try {
                progressEditText.text.toString().toFloat()
            } catch(_: NumberFormatException) {
                0f
            }
            progressBarCustomView.value = value
            progressEditText.text = null
        }
    }
}