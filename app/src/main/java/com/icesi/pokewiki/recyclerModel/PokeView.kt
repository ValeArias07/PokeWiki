package com.example.mysocialnetwork.recyclerModel

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.icesi.pokewiki.R
import com.icesi.pokewiki.model.Pokemon

class PokeView(itemView: View) : RecyclerView.ViewHolder(itemView) {
    ///State
    var pokemon: Pokemon? = null
    var uri: Uri?= null
    /// UI Components
    var pokeName : TextView = itemView.findViewById(R.id.pokeNameText)
    var pokeDate: TextView = itemView.findViewById(R.id.datePokeText)
    var pokeImage: ImageView = itemView.findViewById(R.id.pokeImage)

}
