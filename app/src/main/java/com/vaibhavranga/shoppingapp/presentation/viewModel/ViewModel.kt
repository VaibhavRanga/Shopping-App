package com.vaibhavranga.shoppingapp.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhavranga.shoppingapp.common.ResultState
import com.vaibhavranga.shoppingapp.domain.model.UserDataModel
import com.vaibhavranga.shoppingapp.domain.useCase.CreateUserUseCase
import com.vaibhavranga.shoppingapp.domain.useCase.SignInWithEmailAndPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val signInWithEmailAndPassword: SignInWithEmailAndPassword
) : ViewModel() {
    private val _createUserState = MutableStateFlow(CreateUserState())
    val createUserState = _createUserState.asStateFlow()

    private val _signInWithEmailAndPasswordState = MutableStateFlow(SignInWithEmailAndPasswordState())
    val signInWithEmailAndPasswordState = _signInWithEmailAndPasswordState.asStateFlow()

    fun createUser(userData: UserDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            createUserUseCase.createUserUseCase(userData = userData).collect { response ->
                when (response) {
                    is ResultState.Error -> _createUserState.value = CreateUserState(isLoading = false, error = response.error)
                    ResultState.Loading -> _createUserState.value = CreateUserState(isLoading = true)
                    is ResultState.Success -> _createUserState.value = CreateUserState(isLoading = false, data = response.data)
                }
            }
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            signInWithEmailAndPassword.signInWithEmailAndPassword(email = email, password = password).collect { response ->
                when (response) {
                    is ResultState.Error -> _signInWithEmailAndPasswordState.value = SignInWithEmailAndPasswordState(isLoading = false, error = response.error)
                    ResultState.Loading -> _signInWithEmailAndPasswordState.value = SignInWithEmailAndPasswordState(isLoading = true)
                    is ResultState.Success -> _signInWithEmailAndPasswordState.value = SignInWithEmailAndPasswordState(isLoading = false, data = response.data)
                }
            }
        }
    }
}

data class CreateUserState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: String? = null
)

data class SignInWithEmailAndPasswordState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: String? = null
)