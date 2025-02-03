package com.vaibhavranga.shoppingapp.domain.useCase

import com.vaibhavranga.shoppingapp.domain.repository.Repository
import javax.inject.Inject

class SignInWithEmailAndPassword @Inject constructor(
    private val repository: Repository
) {
    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ) =
        repository.signInWithEmailAndPassword(
            email = email,
            password = password
        )
}