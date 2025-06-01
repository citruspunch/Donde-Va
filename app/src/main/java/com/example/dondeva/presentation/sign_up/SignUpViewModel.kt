package com.example.dondeva.presentation.sign_up

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dondeva.R
import com.example.dondeva.SCAN_SCREEN
import com.example.dondeva.SIGN_UP_SCREEN
import com.example.dondeva.domain.service.AccountService
import com.example.dondeva.presentation.DondeVaAppViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val accountService: AccountService
) : DondeVaAppViewModel() {
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val confirmPassword = MutableStateFlow("")
    val isSigningUp = MutableStateFlow(false)

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
        isSigningUp.value = true
        viewModelScope.launch {
            try {
                if (password.value != confirmPassword.value) {
                    throw Exception("Passwords do not match")
                }
                accountService.signUp(email.value, password.value)
                openAndPopUp(SCAN_SCREEN, SIGN_UP_SCREEN)
            } catch (exception: Throwable) {
                Log.e("Sign Up", "An error occurred when trying to sign up", exception)
                when (exception.message) {
                    "Given String is empty or null" -> showError(R.string.fill_all_fields)
                    "Passwords do not match" -> showError(R.string.passwords_do_not_match)
                    "The email address is already in use by another account." -> showError(R.string.account_already_exists)
                    "The given password is invalid. [ Password should be at least 6 characters ]" -> showError(
                        R.string.password_must_be_at_least_six_characters_long
                    )

                    else -> showError(R.string.incorrect_credentials)
                }
            } finally {
                isSigningUp.value = false
            }
        }
    }
}

class SignUpViewModelFactory(
    private val accountService: AccountService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignUpViewModel(accountService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
