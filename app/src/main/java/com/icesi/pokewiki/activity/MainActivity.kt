package com.icesi.pokewiki.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.icesi.pokewiki.databinding.MenuActivityBinding
import com.icesi.pokewiki.model.Pokemon

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MenuActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MenuActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = "https://pokeapi.co/api/v2/pokemon/pikachu"
        var http = HTTPSWebUtilDomi().GETRequest(url)

        val gson = Gson()
        Log.e(">>>",gson.fromJson(http, Pokemon::class.java).toString())


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