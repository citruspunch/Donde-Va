package com.example.dondeva.domain.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class GarbageItem(
    @DocumentId val id: String = "",
    val label: String = "",          // Resultado del modelo
    val category: String = "",       // reciclable, organico o inorganico
    val userId: String = "",
    val timestamp: Timestamp = Timestamp.now() // Para ordenar historial
)
