package com.example.dondeva.domain.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class GarbageItem(
    @DocumentId val id: String = "",
    val label: String = "",          // Resultado del modelo
    val category: String = "",       // reciclable, organico o inorganico
    val userId: String = "",
    val scanningTime: Timestamp = Timestamp.now() // Para ordenar historial
)

fun GarbageItem.getShortLabel(maxLength: Int = 30): String {
    return if (label.length > maxLength) label.substring(0, maxLength) + "..." else label
}
