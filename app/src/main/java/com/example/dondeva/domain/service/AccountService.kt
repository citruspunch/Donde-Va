package com.example.dondeva.domain.service

import com.example.dondeva.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUser: Flow<User?>
    val currentUserId: String
    fun hasUser(): Boolean
    suspend fun signIn(email: String, password: String)
    suspend fun signUp(email: String, password: String)
    suspend fun signOut()
    suspend fun signInWithGoogle(idToken: String)
    suspend fun signUpWithGoogle(idToken: String)
    suspend fun deleteAccount()
}