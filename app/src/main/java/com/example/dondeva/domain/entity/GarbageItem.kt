package com.example.dondeva.domain.entity

import com.example.dondeva.presentation.scanning.domain.GarbageType
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class GarbageItem(
    @set:DocumentId var id: String = "",
    val userId: String,
    val type: GarbageType,
    val scanningTime: Timestamp = Timestamp.now(),
) {
    // Used by Firestore to create new instances when reading
    constructor() : this(id = "", userId = "", type = GarbageType.MISCELLANEOUS)
}

//fun GarbageItem.getShortLabel(maxLength: Int = 30): String =
//    if (label.length > maxLength) label.substring(0, maxLength) + "..." else label
