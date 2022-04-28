package com.example.mysocialnetwork.recyclerModel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.icesi.pokewiki.R
import com.icesi.pokewiki.model.Pokemon

class PokeView(itemView: View) : RecyclerView.ViewHolder(itemView) {
    ///State
    var clickRowListener: ClickRowListener? = null
    var pokemon: Pokemon? = null
    var uri: Uri?= null
    /// UI Components
    var pokeName : TextView = itemView.findViewById(R.id.pokeNameText)
    var pokeDate: TextView = itemView.findViewById(R.id.datePokeText)
    var pokeImage: ImageView = itemView.findViewById(R.id.pokeImage)
    var pokerowLayout: ConstraintLayout = itemView.findViewById(R.id.pokerowLayout)

    init {
        pokerowLayout.setOnClickListener {
            clickRowListener?.onClickRowListener(pokeName.text.toString())
        }
    }

    interface ClickRowListener{
        fun onClickRowListener(pokeName: String)
    }
}




