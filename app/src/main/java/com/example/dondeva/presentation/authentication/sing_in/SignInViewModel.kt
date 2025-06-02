package com.example.dondeva.presentation.authentication.sing_in

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dondeva.R
import com.example.dondeva.SCAN_SCREEN
import com.example.dondeva.SIGN_IN_SCREEN
import com.example.dondeva.SIGN_UP_SCREEN
import com.example.dondeva.domain.service.AccountService
import com.example.dondeva.presentation.DondeVaAppViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SignInViewModel(
    private val accountService: AccountService,
) : DondeVaAppViewModel() {
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val isSigningIn = MutableStateFlow(false)

    fun updateEmail(newEmail: String) {
        email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        password.value = newPassword
    }

    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
        isSigningIn.value = true
        viewModelScope.launch {
            try {
                accountService.signIn(email.value, password.value)
                openAndPopUp(SCAN_SCREEN, SIGN_IN_SCREEN)
            } catch (exception: Throwable) {
                Log.e("Sign In", "An error occurred when trying to sign in", exception)
                when (exception.message) {
                    "Given String is empty or null" -> showError(R.string.fill_all_fields)
                    else -> showError(R.string.incorrect_credentials)
                }
            } finally {
                isSigningIn.value = false
            }
        }
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp(SIGN_UP_SCREEN, SIGN_IN_SCREEN)
    }

    fun handleGoogleSignInWithBottomSheet(
        context: Context,
        openAndPopUp: (String, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(true)
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = CredentialManager.create(context).getCredential(
                    request = request,
                    context = context
                )

                val googleCredential =
                    GoogleIdTokenCredential.createFrom(result.credential.data)

                accountService.signInWithGoogle(googleCredential.idToken)
                openAndPopUp(SCAN_SCREEN, SIGN_IN_SCREEN)

            } catch (e: NoCredentialException) {
                // Retry without filter
                retryGoogleSignInWithoutFilter(context, openAndPopUp)
            } catch (e: GetCredentialException) {
                Log.e(ERROR_TAG, "GetCredentialException: ${e.message}")
                showError(R.string.get_credential_error)
            } catch (e: Exception) {
                Log.e(ERROR_TAG, "Google Sign-In Failed: ${e.message}")
                showError(R.string.unexpected_credential)
            }
        }
    }

    private fun retryGoogleSignInWithoutFilter(
        context: Context,
        openAndPopUp: (String, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = CredentialManager.create(context).getCredential(
                    request = request,
                    context = context
                )

                val googleCredential =
                    GoogleIdTokenCredential.createFrom(result.credential.data)

                accountService.signInWithGoogle(googleCredential.idToken)
                openAndPopUp(SCAN_SCREEN, SIGN_IN_SCREEN)

            } catch (e: Exception) {
                Log.e(ERROR_TAG, "Final retry failed: ${e.message}")
                showError(R.string.unexpected_credential)
            }
        }
    }
}

class SignInViewModelFactory(
    private val accountService: AccountService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return SignInViewModel(accountService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}