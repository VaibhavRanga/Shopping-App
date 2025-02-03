package com.vaibhavranga.shoppingapp.domain.repository

import com.vaibhavranga.shoppingapp.common.ResultState
import com.vaibhavranga.shoppingapp.domain.model.UserDataModel
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun registerUserWithEmailAndPassword(userData: UserDataModel): Flow<ResultState<String>>

    suspend fun signInWithEmailAndPassword(email: String, password: String): Flow<ResultState<String>>
}