package com.icesi.pokewiki.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.icesi.pokewiki.recyclerModel.PokeAdapter
import com.icesi.pokewiki.recyclerModel.PokeView
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.icesi.pokewiki.R
import com.icesi.pokewiki.databinding.MenuActivityBinding
import com.icesi.pokewiki.model.Pokemon
import com.icesi.pokewiki.model.PokemonResponse
import com.icesi.pokewiki.util.HTTPSWebUtilDomi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() , PokeView.ClickRowListener {
    private val  maxPokemonsInApi = 898
    private val binding: MenuActivityBinding by lazy { MenuActivityBinding.inflate(layoutInflater) }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),::onResult)
    private lateinit var currentUser: String
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: PokeAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.extras?.getString("currentUser") == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            currentUser = intent.extras?.getString("currentUser").toString()
            setContentView(binding.root)
            initRecycler()

            binding.catchButton.setOnClickListener {
                val name = binding.catchText.text.toString()
                if (name == "") Toast.makeText(this, R.string.pokename_empty, Toast.LENGTH_LONG).show()
                else  getRequest(name, "catch")
            }

            binding.watchButton.setOnClickListener {
                val name = binding.catchText.text.toString()
                if (name == "") Toast.makeText(this, R.string.pokename_empty, Toast.LENGTH_LONG).show()
                else  getRequest(name, "watch")
            }

            binding.searchButton.setOnClickListener {
                val name = binding.searchText.text.toString()
                if (name == "") Toast.makeText(this, R.string.pokename_empty, Toast.LENGTH_LONG).show()
                else  searchPokemon(name)
            }

            binding.randomButton.setOnClickListener {
                val random = (Math.random() * (maxPokemonsInApi)).toInt()
                getRequest(random.toString(), "watch")
            }

            binding.exitButtonM.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    override fun onClickRowListener(pokemon: Pokemon) { startNewActivity(Gson().toJson(pokemon),"click") }

    //CALLBACK
    private fun onResult(result:ActivityResult){
        if(result.resultCode == RESULT_OK){
            val data = result.data
            val mode = data?.extras?.getString("mode")
            val pokeJson = data?.extras?.getString("currentPokemon")
            val pokemon = Gson().fromJson(pokeJson,Pokemon::class.java)
            when(mode){
                "catch" -> savePokemon(pokemon)
                "leave" -> deletePokemon(pokemon.name)
            }
        }
    }

    private fun initRecycler() {
        layoutManager = LinearLayoutManager(this)
        binding.recycler.layoutManager = layoutManager
        binding.recycler.setHasFixedSize(true)
        adapter = PokeAdapter()
        adapter.clickRowListener = this
        loadList()
        binding.recycler.adapter = adapter
    }

    private fun loadList() {
        val pokeList: ArrayList<Pokemon> = arrayListOf()
        Firebase.firestore.collection("users")
            .document(currentUser)
            .collection("pokemon")
            .orderBy("date", Query.Direction.ASCENDING)
            .get().addOnCompleteListener { task ->
                for (doc in task.result!!) {
                    pokeList.add(doc.toObject(Pokemon::class.java))
                }
                adapter.setPokemonList(pokeList)
                adapter.notifyDataSetChanged()
            }
    }

    private fun startNewActivity(pokeGson: String, mode: String){
        val intent = Intent(this, CatchActivity::class.java).apply {
            putExtra("mode", mode)
            putExtra("currentUser", currentUser)
            putExtra("currentPokemon", pokeGson)
        }
        launcher.launch(intent)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getRequest(pokeName: String, mode: String){
        val gson = Gson()
        lifecycleScope.launch(Dispatchers.IO) {
            val url = "https://pokeapi.co/api/v2/pokemon/$pokeName"
            val info = HTTPSWebUtilDomi().GETRequest(url)

            withContext(Dispatchers.Main){
                val pokeResponse = gson.fromJson(info, PokemonResponse::class.java)
                transformData(pokeResponse, mode)
            }
        }
    }

    private fun transformData(pokeResponse: PokemonResponse, mode: String) {
        val date = SimpleDateFormat(" d 'de' MMMM 'de' yyyy \n hh:mm:ss", Locale("es", "COL"))
        val pokemon =  Pokemon(
            pokeResponse.name,
            pokeResponse.types[0].type.name,
            Integer.parseInt(pokeResponse.stats[0].base_stat),
            Integer.parseInt(pokeResponse.stats[1].base_stat),
            Integer.parseInt(pokeResponse.stats[2].base_stat),
            Integer.parseInt(pokeResponse.stats[3].base_stat),
            date.format(Date()),
            pokeResponse.sprites.other.home.front_default
        )
        when(mode){
            "watch" ->  startNewActivity(Gson().toJson(pokemon),"watch")
            "catch" ->  savePokemon(pokemon)
        }
    }

    private fun savePokemon(pokemon:Pokemon){
        Firebase.firestore.collection("users").document(currentUser).collection("pokemon")
            .whereEqualTo("name", pokemon.name).get().addOnCompleteListener{ task ->
                if(task.result?.size() != 0){
                    Toast.makeText(this, R.string.pokemon_exist, Toast.LENGTH_SHORT).show()
                }else{
                    Firebase.firestore.collection("users")
                    .document(currentUser)
                    .collection("pokemon")
                    .document(pokemon.name)
                    .set(pokemon).addOnCompleteListener{
                        adapter.addPokemon(pokemon)
                        adapter.notifyDataSetChanged()
                        Toast.makeText(this, R.string.success_catch, Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

    private fun deletePokemon(pokeName: String) {
        Firebase.firestore.collection("users")
            .document(currentUser)
            .collection("pokemon")
            .document(pokeName)
            .delete().addOnCompleteListener {
                adapter.deletePokemon(pokeName)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, R.string.success_leave, Toast.LENGTH_LONG).show()
            }
    }

    private fun searchPokemon(pokeName: String){
        Firebase.firestore.collection("users").document(currentUser).collection("pokemon")
            .whereEqualTo("name", pokeName).get().addOnCompleteListener{ task ->
            if(task.result?.size() != 0){
                lateinit var pokemonFound: Pokemon
                for(document in task.result!!) pokemonFound = document.toObject(Pokemon::class.java)
                startNewActivity(Gson().toJson(pokemonFound),"search")
            }else{
                Toast.makeText(this, R.string.pokemon_not_found, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
