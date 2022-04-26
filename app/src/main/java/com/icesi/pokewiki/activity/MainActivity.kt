package com.icesi.pokewiki.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.icesi.pokewiki.databinding.MenuActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MenuActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MenuActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onResult)
        binding.catchButton.setOnClickListener {
            val intent = Intent(this, CatchActivity::class.java).apply {
                putExtra("mode", "catch")
            }

            launcher.launch(intent)
        }

        binding.searchButton.setOnClickListener {
            val intent = Intent(this, CatchActivity::class.java).apply {
                putExtra("mode", "search")
            }

            launcher.launch(intent)
        }

        binding.exitButtonM.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            launcher.launch(intent)
        }
    }



    private fun onResult(activityResult: ActivityResult?) {

    }
}