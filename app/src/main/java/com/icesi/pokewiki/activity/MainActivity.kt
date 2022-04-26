package com.icesi.pokewiki.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.icesi.pokewiki.databinding.MenuActivityBinding
import com.icesi.pokewiki.model.Pokemon
import com.icesi.pokewiki.model.PokemonResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MenuActivityBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MenuActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onResult)
        binding.catchButton.setOnClickListener {
            var pokemon = binding.catchText.text

            val intent = Intent(this, CatchActivity::class.java).apply {
                putExtra("mode", "catch")
                putExtra("currentPokemon", pokemon)
            }

            launcher.launch(intent)
        }

        binding.searchButton.setOnClickListener {
            val intent = Intent(this, CatchActivity::class.java).apply {
                putExtra("mode", "search")
            }

            launcher.launch(intent)
        }

        binding.randomButton.setOnClickListener {
            val random:Int = (Math.random()*(50)).toInt();
            Log.e(">>>", ""+random)
            val intent = Intent(this, CatchActivity::class.java).apply {
                putExtra("mode", "catch")
                putExtra("currentPokemon", ""+random)
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