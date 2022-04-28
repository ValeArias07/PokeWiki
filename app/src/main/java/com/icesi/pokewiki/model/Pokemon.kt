package com.icesi.pokewiki.model

import java.io.Serializable

data class Pokemon (

    val name: String="",
    val type: String="",
    val life: Int=0,
    val attack: Int=0,
    val defense: Int=0,
    val speed: Int=0,
    val date: String="",
    val img: String=""
        )