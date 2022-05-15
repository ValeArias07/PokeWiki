package com.icesi.pokewiki.recyclerModel

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.icesi.pokewiki.R
import com.icesi.pokewiki.model.Pokemon

class PokeAdapter : RecyclerView.Adapter<PokeView>() {

    lateinit var clickRowListener: PokeView.ClickRowListener
    private var pokeList = ArrayList<Pokemon>()
    private lateinit var context:Context

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokeView {
        //Inflater: XML-> View
        val inflater = LayoutInflater.from(parent.context)
        //row = View
        val row = inflater.inflate(R.layout.pokerow, parent, false)
        val pokeView = PokeView(row)
        pokeView.clickRowListener = clickRowListener
        return pokeView
    }

    //Rellena un esqueleto con los valores del algún objeto
    //Se repite dependiendo cuantos elementos tenga el arreglo
    override fun onBindViewHolder(skeleton: PokeView, position: Int) {
        val pokemon = pokeList[position]
        skeleton.pokemon = pokemon
        skeleton.pokeName.text = pokemon.name.replaceFirstChar {it.uppercase()}
        skeleton.pokeDate.text = pokemon.date
        Glide.with(context).load(pokemon.img).into(skeleton.pokeImage)
    }

    override fun getItemCount(): Int { return pokeList.size }

    fun addPokemon(pokemon: Pokemon){ pokeList.add(pokemon) }

    fun deletePokemon(pokeName:String){
        for(pok in pokeList){
            if(pok.name == pokeName) {
                pokeList.remove(pok)
                break
            }
        }
    }

    fun setPokemonList(list:ArrayList<Pokemon>){ pokeList=list }
}