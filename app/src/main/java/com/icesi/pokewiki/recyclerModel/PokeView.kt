package com.icesi.pokewiki.recyclerModel

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.icesi.pokewiki.R
import com.icesi.pokewiki.model.Pokemon

class PokeView(itemView: View) : RecyclerView.ViewHolder(itemView) {
    ///State
    lateinit var clickRowListener: ClickRowListener
    lateinit var pokemon: Pokemon
    /// UI Components
    var pokeNameLeft : TextView = itemView.findViewById(R.id.pokeNameTextLeft)
    var pokeDateLeft: TextView = itemView.findViewById(R.id.datePokeTextLeft)
    var pokeImageLeft: ImageView = itemView.findViewById(R.id.pokeImageLeft)
    var pokeNameRight : TextView = itemView.findViewById(R.id.pokeNameTextRight)
    var pokeDateRight: TextView = itemView.findViewById(R.id.datePokeTextRight)
    var pokeImageRight: ImageView = itemView.findViewById(R.id.pokeImageRight)
    var pokerowLayout: ConstraintLayout = itemView.findViewById(R.id.pokerowLayout)

    init {
        pokerowLayout.setOnClickListener {
            clickRowListener.onClickRowListener(pokemon)
        }
    }

    interface ClickRowListener{
        fun onClickRowListener(pokemon: Pokemon)
    }
}




