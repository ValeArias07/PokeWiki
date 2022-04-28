package com.icesi.pokewiki.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysocialnetwork.recyclerModel.PokeAdapter
import com.example.mysocialnetwork.recyclerModel.PokeView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.icesi.pokewiki.databinding.MenuActivityBinding
import com.icesi.pokewiki.model.Pokemon
import java.util.ArrayList

class MainActivity : AppCompatActivity() , PokeView.ClickRowListener {
    private lateinit var currentUser: String
    private lateinit var binding: MenuActivityBinding
    private lateinit var adapter: PokeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MenuActivityBinding.inflate(layoutInflater)
        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onResult)


        if(intent.extras?.getString("currentUser")==null){
            val intent = Intent(this, LoginActivity::class.java)
            launcher.launch(intent)
        }else{
            currentUser = intent.extras?.getString("currentUser").toString()
            setAdapter()
            loadList()
            setContentView(binding.root)
        }

        binding.catchButton.setOnClickListener {

            var name: String = binding.catchText.text.toString()
           if(name !=""){
               val intent = Intent(this, CatchActivity::class.java).apply {
                   putExtra("mode", "catch")
                   putExtra("currentUser", currentUser)
                   putExtra("currentPokemon", ""+binding.catchText.text)
               }
           }else{
               Toast.makeText(applicationContext, "Escribe un nombre o numero de Pokemón", Toast.LENGTH_LONG).show()
           }
            launcher.launch(intent)
        }

        binding.searchButton.setOnClickListener {
            val intent = Intent(this, CatchActivity::class.java).apply {
                putExtra("mode", "search")
                putExtra("currentUser", currentUser)
            }

            launcher.launch(intent)
        }

        binding.randomButton.setOnClickListener {
            val random: Int = (Math.random()*(50)).toInt();
            val intent = Intent(this, CatchActivity::class.java).apply {
                putExtra("mode", "catch")
                putExtra("currentUser", currentUser)
                putExtra("currentPokemon", ""+random)
            }

            launcher.launch(intent)
        }

        binding.exitButtonM.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            launcher.launch(intent)
        }
    }

    private fun setAdapter() {
        var recycler = binding.recycler
        adapter = PokeAdapter()
        adapter.clickRowListener = this
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun loadList() {
        var pokeList: ArrayList<Pokemon> = arrayListOf()

        Firebase.firestore.collection("users")
            .document(currentUser)
            .collection("pokemon")
            .get().addOnCompleteListener { task ->
                for (doc in task.result!!) {
                    pokeList.add(doc.toObject(Pokemon::class.java))

                    }
            }.continueWith {
                adapter.setArray(pokeList)
                Log.e("POKE", pokeList.toString())
                adapter.notifyDataSetChanged()
            }
    }


    fun showPoke(){
        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onResult)
            val intent = Intent(this, CatchActivity::class.java).apply{
                putExtra("mode", "search")
                putExtra("currentUser", currentUser)
                putExtra("currentPokemon", intent.extras?.getString("pokemon").toString())
            }
            launcher.launch(intent)
    }
     fun onResult(activityResult: ActivityResult?) {

    }

    override fun onClickRowListener(pokeName: String) {

        //// Buscar pokemon y de ahi pasarlo
        /**
         *   val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onResult)
        val intent = Intent(this, CatchActivity::class.java).apply{
        putExtra("mode", "search")
        putExtra("currentUser", currentUser)
        ///putExtra("currentPokemon", )
        }
        launcher.launch(intent)
         */
        Toast.makeText(applicationContext, "Si sirve" + pokeName, Toast.LENGTH_SHORT).show()
    }
}