package com.icesi.pokewiki.recyclerModel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.icesi.pokewiki.R
import com.icesi.pokewiki.databinding.MenuActivityBinding
import com.icesi.pokewiki.model.Pokemon


class PokeAdapter() : RecyclerView.Adapter<PokeView>() {

    var clickRowListener: PokeView.ClickRowListener? = null
    private lateinit var binding: MenuActivityBinding
    private val pokemons = ArrayList<Pokemon>()

    fun addPokemon(pokemon: Pokemon){
        pokemons.add(pokemon)
    }

    fun setArray(pokemon: ArrayList<Pokemon>){
        for(num in 0..pokemon.size-1){
            pokemons.add(pokemon.get(num))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokeView {
        var inflater = LayoutInflater.from(parent.context)
        binding = MenuActivityBinding.inflate(inflater)
        val row = inflater.inflate(R.layout.pokerow, parent, false)
        val pokeView = PokeView(row)
        pokeView.clickRowListener = clickRowListener
        return pokeView
    }

    override fun onBindViewHolder(skeleton: PokeView, position: Int) {
        val pokemon = pokemons[position]
        skeleton.pokemon = pokemon
        skeleton.pokeName.text = pokemon.name.replaceFirstChar {it.uppercase()}
        skeleton.pokeDate.text = pokemon.date
        Glide.with(binding.root).load(pokemon.img).into(skeleton.pokeImage)
    }

    override fun getItemCount(): Int {
        return pokemons.size
    }
}