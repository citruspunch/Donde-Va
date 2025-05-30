package com.example.dondeva.domain.service

import com.example.dondeva.domain.entity.GarbageItem
import kotlinx.coroutines.flow.Flow

interface StorageService {
    suspend fun createGarbageItem(garbageItem: GarbageItem)
    suspend fun deleteGarbageItem(itemId: String)
    fun garbageItemsByUser(userId: String): Flow<List<GarbageItem>>
}