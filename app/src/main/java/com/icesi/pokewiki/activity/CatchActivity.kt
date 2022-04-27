package com.icesi.pokewiki.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
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
        currentUser = intent.extras?.getString("currentUser").toString()
        binding = InfoActivityBinding.inflate(layoutInflater)
        adapter = PokeAdapter()
        var gson = Gson()

        if(intent.extras?.getString("mode") == "catch"){
            binding.leaveButton.isVisible=false
            lifecycleScope.launch(Dispatchers.IO){
                var url ="https://pokeapi.co/api/v2/pokemon/"+intent.extras?.getString("currentPokemon")
                var info = HTTPSWebUtilDomi().GETRequest(url)
                Log.e(">>>", info)
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
            val intent = Intent(this, MainActivity::class.java).apply{
                putExtra("currentUser", currentUser)
            }

            launcher.launch(intent)
        }

        binding.leaveButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply{
                putExtra("currentUser", currentUser)

            }
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
            pokeResponse?.sprites.other.home.front_default.toString()
        )
        setData(pokemon)
    }
    private fun setData(pokemon: Pokemon) {
        binding.nameText.text = pokemon.name
        binding.typeText.text =(pokemon.type)
        binding.defenseText.text =(""+pokemon.defense)
        binding.attackText.text =(""+pokemon.attack)
        binding.lifeText.text =(""+pokemon.life)
        binding.speedText.text =(""+pokemon.speed)
        Glide.with(applicationContext).load(pokemon.img).into(binding.pokeInfoImage)
    }

    private fun savePokemon(pokemon: Pokemon) {
        val add = HashMap<String, Any>()
        val user = intent.extras?.getString("currentUser").toString()
        add["name"] = pokemon.name
        add["type"] = pokemon.type
        add["defense"] = pokemon.defense
        add["attack"] = pokemon.attack
        add["life"] = pokemon.life
        add["speed"] = pokemon.speed
        add["img"] = pokemon.img

        Firebase.firestore.collection("users")
            .document(user)
            .collection("pokemon")
            .document(pokemon.name)
            .set(add)
        adapter.addPokemon(pokemon)
        adapter.notifyDataSetChanged()
    }

    }

    private fun addNewPokemon(){

    }
    fun onResult(activityResult: ActivityResult?) {

    }

