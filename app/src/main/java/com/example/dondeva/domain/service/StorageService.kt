package com.example.dondeva.domain.service

import com.example.dondeva.domain.entity.GarbageItem
import kotlinx.coroutines.flow.Flow

interface StorageService {
    val garbageItemsByCurrentUser: Flow<List<GarbageItem>>
    suspend fun createGarbageItem(garbageItem: GarbageItem)
    suspend fun deleteGarbageItem(itemId: String)
    suspend fun readGarbageItem(itemId: String): GarbageItem?
}