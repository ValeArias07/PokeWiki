package com.icesi.pokewiki.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.icesi.pokewiki.databinding.InfoActivityBinding
import com.icesi.pokewiki.model.Pokemon

class CatchActivity : AppCompatActivity() {
    private val binding: InfoActivityBinding by lazy {InfoActivityBinding.inflate(layoutInflater)}
    private lateinit var currentUser: String
    private lateinit var currentPokemon: Pokemon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pokeJson = (intent.extras?.getString("currentPokemon")).toString()
        currentUser = intent.extras?.getString("currentUser").toString()
        currentPokemon = Gson().fromJson(pokeJson,Pokemon::class.java)
        when(intent.extras?.getString("mode")){
            "watch" ->  binding.leaveButton.isVisible = false
            "click" ->  binding.catchPokButton.isVisible = false
            "search" -> binding.catchPokButton.isVisible = false
        }
        setData(currentPokemon)

        binding.catchPokButton.setOnClickListener { goToMain("catch") }
        binding.leaveButton.setOnClickListener { goToMain("leave") }
        binding.exitButtonM.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setData(pokemon: Pokemon) {
        binding.nameText.text = pokemon.name.uppercase()
        binding.typeText.text = (pokemon.type)
        binding.defenseText.text = ("${pokemon.defense}")
        binding.attackText.text = ("${pokemon.attack}")
        binding.lifeText.text = ("${pokemon.life}")
        binding.speedText.text = ("${pokemon.speed}")
        Glide.with(applicationContext).load(pokemon.img).into(binding.pokeInfoImage)
        binding.pokeInfoImage.maxWidth = 100
        binding.pokeInfoImage.maxHeight = 100
        setContentView(binding.root)
    }

    private fun goToMain(mode:String){
        val intent = Intent().apply {
            putExtra("currentPokemon", Gson().toJson(currentPokemon))
            putExtra("mode", mode)
        }
        setResult(Activity.RESULT_OK,intent)
        finish()
    }
}
