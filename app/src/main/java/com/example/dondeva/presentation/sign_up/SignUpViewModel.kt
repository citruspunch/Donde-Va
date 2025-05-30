package com.example.dondeva.presentation.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dondeva.domain.service.AccountService
import com.example.dondeva.presentation.DondeVaAppViewModel
import com.example.dondeva.SCAN_SCREEN
import com.example.dondeva.SIGN_UP_SCREEN
import kotlinx.coroutines.flow.MutableStateFlow

class SignUpViewModel(
    private val accountService: AccountService
) : DondeVaAppViewModel() {
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val confirmPassword = MutableStateFlow("")

    fun updateEmail(newEmail: String) {
        email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        password.value = newPassword
    }

    fun updateConfirmPassword(newConfirmPassword: String) {
        confirmPassword.value = newConfirmPassword
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            if (password.value != confirmPassword.value) {
                throw Exception("Passwords do not match")
            }

            accountService.signUp(email.value, password.value)
            openAndPopUp(SCAN_SCREEN, SIGN_UP_SCREEN)
        }
    }
}

class SignUpViewModelFactory(
    private val accountService: AccountService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SignUpViewModel(accountService) as T
    }
}
