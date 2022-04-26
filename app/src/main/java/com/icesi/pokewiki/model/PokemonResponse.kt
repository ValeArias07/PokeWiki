package com.icesi.pokewiki.model

import android.hardware.camera2.params.Capability

data class PokemonResponse (
    val name: String,
    val stats: ArrayList<Stat>,
    val abilities: ArrayList<Ability>,
    val sprites: Sprite
)

data class Ability (
    val ability: AbilityData
)

class AbilityData {
    val name: String=""
}

data class Stat(
    val base_stat: String="",
    val stat: StatN)

data class StatN (
    val name: String=""
)

data class Sprite(
    val other: Artwork
)

data class Artwork (
    val dream_world: LinkArt
        )

class LinkArt (
    val front_default: String=""
        )
