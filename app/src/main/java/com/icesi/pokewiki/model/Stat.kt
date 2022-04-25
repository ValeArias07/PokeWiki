package com.icesi.pokewiki.model

data class Stat (
    val base_stat: String,
    val stat: ArrayList<StatN>
)

data class StatN (
    val name: String

)

