package com.example.dondeva.presentation.authentication.sign_up

import android.content.Context
import androidx.credentials.CredentialManager
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dondeva.R
import com.example.dondeva.SCAN_SCREEN
import com.example.dondeva.SIGN_UP_SCREEN
import com.example.dondeva.domain.service.AccountService
import com.example.dondeva.presentation.DondeVaAppViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
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


    fun handleGoogleSignUpWithBottomSheet(
        context: Context,
        openAndPopUp: (String, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val signInWithGoogleOption = GetSignInWithGoogleOption
                    .Builder(serverClientId = context.getString(R.string.default_web_client_id))
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(signInWithGoogleOption)
                    .build()

                val result = CredentialManager.create(context).getCredential(
                    request = request,
                    context = context
                )

                val googleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(result.credential.data)
                accountService.signUpWithGoogle(googleIdTokenCredential.idToken)
                openAndPopUp(SCAN_SCREEN, SIGN_UP_SCREEN)

            } catch (e: NoCredentialException) {
                // Retry without filtering
                handleGoogleSignUpWithBottomSheetWithoutFilter(context, openAndPopUp)
            } catch (e: GetCredentialException) {
                Log.d(ERROR_TAG, e.message.orEmpty())
                showError(R.string.get_credential_error)
            } catch (e: Exception) {
                Log.d(ERROR_TAG, e.message.orEmpty())
                showError(R.string.unexpected_credential)
            }
        }
    }

    private fun handleGoogleSignUpWithBottomSheetWithoutFilter(
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

                val googleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(result.credential.data)
                accountService.signUpWithGoogle(googleIdTokenCredential.idToken)
                openAndPopUp(SCAN_SCREEN, SIGN_UP_SCREEN)

            } catch (e: NoCredentialException) {
                Log.e(ERROR_TAG, e.message.toString())
                showError(R.string.no_accounts_error)
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
