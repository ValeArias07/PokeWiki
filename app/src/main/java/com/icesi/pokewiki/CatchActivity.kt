package com.icesi.pokewiki

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.icesi.pokewiki.databinding.InfoActivityBinding


class CatchActivity : AppCompatActivity() {

    private lateinit var binding: InfoActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = InfoActivityBinding.inflate(layoutInflater)

        if(intent.extras?.getString("mode") == "catch"){
            binding.leaveButton.isVisible=false
        }else{
            binding.catchPokButton.isVisible=false
        }

        setContentView(binding.root)

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onResult)

        binding.catchPokButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)

            launcher.launch(intent)
        }

        binding.leaveButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)

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