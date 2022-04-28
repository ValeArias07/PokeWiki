package com.icesi.pokewiki.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.mysocialnetwork.recyclerModel.PokeAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
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
    private lateinit var currentUser: String
    private lateinit var pokeResponse: PokemonResponse
    private lateinit var binding: InfoActivityBinding
    private lateinit var adapter: PokeAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var pokename = (intent.extras?.getString("currentPokemon")).toString()
        currentUser = intent.extras?.getString("currentUser").toString()

        binding = InfoActivityBinding.inflate(layoutInflater)
        adapter = PokeAdapter()

        if (intent.extras?.getString("mode") == "watch") {
            binding.leaveButton.isVisible = false
            getRequest(pokename, true)

        }else if(intent.extras?.getString("mode") == "catch") {
            getRequest(pokename, false)
            goToMain()

        } else if (intent.extras?.getString("mode") == "search") {
            binding.catchPokButton.isVisible = false
            search(pokename)
        }

        binding.catchPokButton.setOnClickListener {
            savePokemon(pokemon)
            goToMain()
        }

        binding.leaveButton.setOnClickListener {
            erasePokemon(binding.nameText.text.toString())
            goToMain()
        }

        binding.exitButtonM.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getRequest(pokeName: String, mode: Boolean){
        var gson = Gson()
        lifecycleScope.launch(Dispatchers.IO) {
            var url =
                "https://pokeapi.co/api/v2/pokemon/" + intent.extras?.getString("currentPokemon")
            var info = HTTPSWebUtilDomi().GETRequest(url)

            withContext(Dispatchers.Main) {
                pokeResponse = gson.fromJson(info, PokemonResponse::class.java)
                transformData(pokeResponse, mode)
            }
        }
    }

    private fun erasePokemon(pokeName: String) {
        Firebase.firestore.collection("users")
            .document(currentUser)
            .collection("pokemon")
            .document(pokeName)
            .delete()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun transformData(pokeResponse: PokemonResponse?, mode: Boolean) {
        val date = SimpleDateFormat(" d 'de' MMMM 'de' yyyy \n hh:mm:ss", Locale("es", "COL"))
        pokemon = Pokemon(
            pokeResponse?.name!!,
            pokeResponse?.abilities.get(1).ability.name,
            Integer.parseInt(pokeResponse?.stats?.get(0)?.base_stat!!),
            Integer.parseInt(pokeResponse?.stats?.get(1)?.base_stat!!),
            Integer.parseInt(pokeResponse?.stats?.get(2)?.base_stat!!),
            Integer.parseInt(pokeResponse?.stats?.get(3)?.base_stat!!),
            date.format(Date()),
            pokeResponse?.sprites.other.home.front_default.toString()
        )
        if(mode){
            setData(pokemon)
        }else{
            savePokemon(pokemon)
        }
    }

    private fun setData(pokemon: Pokemon) {
        binding.nameText.text = pokemon.name
        binding.typeText.text = (pokemon.type)
        binding.defenseText.text = ("" + pokemon.defense)
        binding.attackText.text = ("" + pokemon.attack)
        binding.lifeText.text = ("" + pokemon.life)
        binding.speedText.text = ("" + pokemon.speed)
        Glide.with(applicationContext).load(pokemon.img).into(binding.pokeInfoImage)
        binding.pokeInfoImage.maxWidth = 100
        binding.pokeInfoImage.maxHeight = 100
        setContentView(binding.root)
    }

    private fun savePokemon(pokemon: Pokemon) {
        val add = HashMap<String, Any>()

        add["name"] = pokemon.name
        add["type"] = pokemon.type
        add["defense"] = pokemon.defense
        add["attack"] = pokemon.attack
        add["life"] = pokemon.life
        add["speed"] = pokemon.speed
        add["img"] = pokemon.img
        add["date"] = pokemon.date

        Firebase.firestore.collection("users")
            .document(currentUser)
            .collection("pokemon")
            .document(pokemon.name)
            .set(add)

        adapter.addPokemon(pokemon)
        adapter.notifyDataSetChanged()
    }


    fun onResult(activityResult: ActivityResult?) {

    }

    fun goToMain(){
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("currentUser", currentUser)
        }
        startActivity(intent)
    }

    fun search(pokename: String){
        var flag: Boolean = true
        Firebase.firestore.collection("users")
            .document(currentUser)
            .collection("pokemon")
            .document(pokename)
            .get()
            .addOnCompleteListener { task ->
                if(task.result.data != null){
                    pokemon = task?.result.toObject(Pokemon::class.java)!!
                }else{
                    flag=false
                }
            }.continueWith{
                if(flag) {
                    setData(pokemon)
                }else{
                    Toast.makeText(applicationContext, "Escribe un nombre o numero de Pokemón", Toast.LENGTH_LONG).show()
                    goToMain()
                }
            }
        }
}
