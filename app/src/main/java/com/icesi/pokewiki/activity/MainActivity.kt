package com.icesi.pokewiki.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.icesi.pokewiki.recyclerModel.PokeAdapter
import com.icesi.pokewiki.recyclerModel.PokeView
import com.google.firebase.firestore.Query
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

        if (intent.extras?.getString("currentUser") == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            currentUser = intent.extras?.getString("currentUser").toString()
            setAdapter()
            loadList()
            setContentView(binding.root)
        }

        binding.catchButton2.setOnClickListener{
            var pokename: String = binding.catchText.text.toString()
            catchPoke("catch",pokename)
        }

        binding.catchButton.setOnClickListener {
            var pokename: String = binding.catchText.text.toString()
            catchPoke("watch",pokename)
        }

        binding.searchButton.setOnClickListener {
            var pokename: String = binding.searchText.text.toString()
            catchPoke("search", pokename)
        }

        binding.randomButton.setOnClickListener {
            val random: Int = (Math.random() * (50)).toInt();
            startNewActivity(""+random, "watch")
        }

        binding.exitButtonM.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startNewActivity(pokename: String, mode: String){
        val intent = Intent(this, CatchActivity::class.java).apply {
            putExtra("mode", mode)
            putExtra("currentUser", currentUser)
            putExtra("currentPokemon", "" + pokename)
        }
        startActivity(intent)
    }

    private fun catchPoke(mode:String, name: String){
        if (name != "") {
            startNewActivity(name, mode)
        } else {
            Toast.makeText(applicationContext, "Escribe un nombre o numero de Pokemón", Toast.LENGTH_LONG).show()
        }
    }

    private fun setAdapter() {
        adapter = PokeAdapter()
        adapter.clickRowListener = this
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter
    }

    private fun loadList() {
        var pokeList: ArrayList<Pokemon> = arrayListOf()
        Firebase.firestore.collection("users")
            .document(currentUser)
            .collection("pokemon")
            .orderBy("date", Query.Direction.ASCENDING)
            .get().addOnCompleteListener { task ->
                for (doc in task.result!!) {
                    pokeList.add(doc.toObject(Pokemon::class.java))
                }
            }.continueWith {
                adapter.setArray(pokeList)
                adapter.notifyDataSetChanged()
            }
    }


    fun onResult(activityResult: ActivityResult?) {

    }

    override fun onClickRowListener(pokeName: String) {
        val intent = Intent(this, CatchActivity::class.java).apply {
            putExtra("mode", "search")
            putExtra("currentUser", currentUser)
            putExtra("currentPokemon", pokeName)
        }
        startActivity(intent)    }
}
