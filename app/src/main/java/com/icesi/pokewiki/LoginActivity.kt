package com.icesi.pokewiki

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.icesi.pokewiki.databinding.LoginActivityBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LoginActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}