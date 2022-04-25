package com.example.mysocialnetwork.recyclerModel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.icesi.pokewiki.R
import com.icesi.pokewiki.databinding.MenuActivityBinding
import com.icesi.pokewiki.model.Pokemon

class PokeAdapter : RecyclerView.Adapter<PokeView>() {

    private lateinit var binding: MenuActivityBinding
    private val pokemons = ArrayList<Pokemon>()

    fun addPost(pokemon: Pokemon){
        pokemons.add(pokemon)
    }

    fun setArray(post: ArrayList<Pokemon>){
        for(num in 0..pokemons.size-1){
            pokemons.add(pokemons.get(num))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokeView {
        var inflater = LayoutInflater.from(parent.context)
        binding = MenuActivityBinding.inflate(inflater)
        val row = inflater.inflate(R.layout.pokerow, parent, false)
        val pokeView = PokeView(row)
        return pokeView
    }

    override fun onBindViewHolder(skeleton: PokeView, position: Int) {
        val pokemon = pokemons[position]
        skeleton.pokemon = pokemon
        skeleton.pokeName.text = pokemon.pName
        skeleton.pokeDate.text = pokemon.pDate
        //skeleton..text = pokemon.pDate
    }

    override fun getItemCount(): Int {
        return pokemons.size
    }
}