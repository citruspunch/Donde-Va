package com.example.dondeva.presentation.scanning.domain

import com.example.dondeva.R

enum class GarbageType(val key: Int, val icon: Int) {
    CARDBOARD(key = R.string.cardboard, icon = R.drawable.box),
    GLASS(key = R.string.glass, icon = R.drawable.wine_glass),
    METAL(key = R.string.metal, icon = R.drawable.wrench),
    PAPER(key = R.string.paper, icon = R.drawable.document),
    PLASTIC(key = R.string.plastic, icon = R.drawable.spray_bottle),
    TRASH(key = R.string.trash, icon = R.drawable.trash_can),
}

data class Classification(
    val type: GarbageType,
    val score: Float,
)
