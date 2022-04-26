package com.icesi.pokewiki.model

import java.io.Serializable

data class Pokemon (

        val pName: String="",
        val pType: String="",
        val pLive: Int=0,
        val pAttack: Int=0,
        val pDefense: Int=0,
        val pSpeed: Int=0,
        val pDate: String="",
        val pImage: String=""
        )