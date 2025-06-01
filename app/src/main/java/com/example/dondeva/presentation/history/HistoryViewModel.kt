package com.example.dondeva.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dondeva.ITEM_DETAIL_SCREEN
import com.example.dondeva.ITEM_ID
import com.example.dondeva.SCAN_SCREEN
import com.example.dondeva.domain.entity.GarbageItem
import com.example.dondeva.domain.service.AccountService
import com.example.dondeva.domain.service.StorageService
import com.example.dondeva.presentation.DondeVaAppViewModel
import com.example.dondeva.SPLASH_SCREEN
import kotlinx.coroutines.flow.Flow

class HistoryViewModel(
    private val accountService: AccountService,
    storageService: StorageService,
) : DondeVaAppViewModel() {
    val garbageItems: Flow<List<GarbageItem>> = storageService.garbageItemsByCurrentUser

    fun initialize(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.currentUser.collect { user ->
                if (user == null) restartApp(SPLASH_SCREEN)
            }
        }
    }

    fun onAddClick(openScreen: (String) -> Unit) {
        openScreen(SCAN_SCREEN)
    }

    fun onItemClick(openScreen: (String) -> Unit, garbageItem: GarbageItem) {
        openScreen("$ITEM_DETAIL_SCREEN?$ITEM_ID$=${garbageItem.id}")
    }

    fun onSignOutClick() {
        launchCatching {
            accountService.signOut()
        }
    }

    fun onDeleteAccountClick() {
        launchCatching {
            accountService.deleteAccount()
        }
    }
}

class HistoryViewModelFactory(
    private val accountService: AccountService,
    private val storageService: StorageService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(accountService, storageService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
