package com.example.dondeva.presentation.sing_in

import com.example.dondeva.SIGN_IN_SCREEN
import com.example.dondeva.SIGN_UP_SCREEN
import com.example.dondeva.presentation.DondeVaAppViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class SignInViewModel @Inject constructor(
    private val accountService: AccountService
) : DondeVaAppViewModel() {
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    fun updateEmail(newEmail: String) {
        email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        password.value = newPassword
    }

    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            accountService.signIn(email.value, password.value)
            openAndPopUp(NOTES_LIST_SCREEN, SIGN_IN_SCREEN)
        }
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp(SIGN_UP_SCREEN, SIGN_IN_SCREEN)
    }
}