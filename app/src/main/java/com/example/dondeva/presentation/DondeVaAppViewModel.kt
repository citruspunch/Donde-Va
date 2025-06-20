package com.example.dondeva.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

open class DondeVaAppViewModel : ViewModel() {
    private val _errorEvents = MutableSharedFlow<Int>()
    val errorEvents: SharedFlow<Int> = _errorEvents

    fun showError(resourceId: Int) = viewModelScope.launch { _errorEvents.emit(resourceId) }

    // Executes coroutines and handles possible errors.
    fun launchCatching(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                Log.d(ERROR_TAG, throwable.message.orEmpty())
            },
            block = block,
        )

    companion object {
        const val ERROR_TAG = "DONDE VA APP ERROR"
    }
}