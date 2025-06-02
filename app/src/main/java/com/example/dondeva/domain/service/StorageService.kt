package com.example.dondeva.domain.service

import com.example.dondeva.domain.entity.GarbageItem
import com.example.dondeva.presentation.scanning.domain.GarbageType
import kotlinx.coroutines.flow.Flow

interface StorageService {
    val garbageItemsByCurrentUser: Flow<List<GarbageItem>>
    suspend fun saveGarbageItem(garbageType: GarbageType): GarbageItem
    suspend fun deleteGarbageItem(itemId: String)
    suspend fun readGarbageItem(itemId: String): GarbageItem?
}