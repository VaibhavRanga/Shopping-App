package com.vaibhavranga.shoppingapp.domain.useCase

import com.vaibhavranga.shoppingapp.domain.model.UserDataModel
import com.vaibhavranga.shoppingapp.domain.repository.Repository
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun createUserUseCase(userData: UserDataModel) =
        repository.registerUserWithEmailAndPassword(userData = userData)
}