package com.example.dondeva.data.impl

import com.example.dondeva.domain.entity.GarbageItem
import com.example.dondeva.domain.service.AccountService
import com.example.dondeva.domain.service.StorageService
import com.example.dondeva.presentation.scanning.domain.GarbageType
import com.google.firebase.Firebase
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await


class StorageServiceImpl(private val auth: AccountService) : StorageService {
    @OptIn(ExperimentalCoroutinesApi::class)
    override val garbageItemsByCurrentUser: Flow<List<GarbageItem>>
        get() = auth.currentUser.flatMapLatest { user ->
            Firebase.firestore
                .collection(GARBAGE_ITEMS_COLLECTION)
                .whereEqualTo(USER_ID_FIELD, user?.id)
                .dataObjects()
        }

    override suspend fun saveGarbageItem(garbageType: GarbageType): GarbageItem {
        val garbageItem = GarbageItem(userId = auth.currentUserId, type = garbageType)
        val savedItemId = Firebase.firestore
            .collection(GARBAGE_ITEMS_COLLECTION)
            .add(garbageItem).await().id
        val savedItem = readGarbageItem(itemId = savedItemId)
        return savedItem!!
    }

    override suspend fun deleteGarbageItem(itemId: String) {
        Firebase.firestore
            .collection(GARBAGE_ITEMS_COLLECTION)
            .document(itemId).delete().await()
    }

    override suspend fun readGarbageItem(itemId: String): GarbageItem? {
        return Firebase.firestore
            .collection(GARBAGE_ITEMS_COLLECTION)
            .document(itemId).get().await().toObject(GarbageItem::class.java)
    }

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val GARBAGE_ITEMS_COLLECTION = "garbageItems"
    }
}