package com.icesi.pokewiki.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.icesi.pokewiki.R
import com.icesi.pokewiki.databinding.InfoActivityBinding
import com.icesi.pokewiki.model.Pokemon
import com.icesi.pokewiki.model.PokemonResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


class CatchActivity : AppCompatActivity() {
    private lateinit var pokemon: Pokemon
    private lateinit var pokeResponse: PokemonResponse
    private lateinit var binding: InfoActivityBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        var gson = Gson()
        super.onCreate(savedInstanceState)
        binding = InfoActivityBinding.inflate(layoutInflater)

        if(intent.extras?.getString("mode") == "catch"){
            binding.leaveButton.isVisible=false
            lifecycleScope.launch(Dispatchers.IO){
                var url ="https://pokeapi.co/api/v2/pokemon/"+intent.extras?.getString("currentPokemon")
                var info = HTTPSWebUtilDomi().GETRequest(url)
                withContext(Dispatchers.Main){
                    pokeResponse = gson.fromJson(info, PokemonResponse::class.java)
                    transformData(pokeResponse)
                }
            }

        }else{
            binding.catchPokButton.isVisible=false
        }

        setContentView(binding.root)

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onResult)

        binding.catchPokButton.setOnClickListener {
            savePokemon(pokemon)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun transformData(pokeResponse: PokemonResponse?) {
        val date = SimpleDateFormat(" d 'de' MMMM 'de' yyyy", Locale("es", "COL"))
        pokemon = Pokemon(
            pokeResponse?.name!!,
            pokeResponse?.abilities.get(1).ability.name,
            Integer.parseInt(pokeResponse?.stats?.get(0)?.base_stat!!),
            Integer.parseInt(pokeResponse?.stats?.get(1)?.base_stat!!),
            Integer.parseInt(pokeResponse?.stats?.get(2)?.base_stat!!),
            Integer.parseInt(pokeResponse?.stats?.get(3)?.base_stat!!),
            date.format(Date()),
            pokeResponse?.sprites.other.dream_world.front_default.toString()
        )
        setData(pokemon)
    }
    private fun setData(pokemon: Pokemon) {
        binding.nameText.text = pokemon.pName
        binding.typeText.text =(pokemon.pType)
        binding.defenseText.text =(""+pokemon.pDefense)
        binding.attackText.text =(""+pokemon.pAttack)
        binding.lifeText.text =(""+pokemon.pLive)
        binding.speedText.text =(""+pokemon.pSpeed)
    }

    private fun savePokemon(pokemon: Pokemon) {
        val add = HashMap<String,Any>()

        add["name"] = pokemon.pName
        add["type"] = pokemon.pType
        add["defense"] = pokemon.pDefense
        add["attack"] = pokemon.pAttack
        add["life"] = pokemon.pLive
        add["speed"] = pokemon.pSpeed
        add["img"] = pokemon.pImage

            Firebase.firestore.collection("users")
            .document("valentina")
            .collection("pokemon")
            .add(add)
    }

    private fun onResult(activityResult: ActivityResult?) {

    }

}