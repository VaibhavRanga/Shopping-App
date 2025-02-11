package com.vaibhavranga.shoppingapp.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhavranga.shoppingapp.common.ResultState
import com.vaibhavranga.shoppingapp.domain.model.CategoryModel
import com.vaibhavranga.shoppingapp.domain.model.ProductModel
import com.vaibhavranga.shoppingapp.domain.model.UserDataModel
import com.vaibhavranga.shoppingapp.domain.useCase.CreateUserUseCase
import com.vaibhavranga.shoppingapp.domain.useCase.GetAllCategoriesUseCase
import com.vaibhavranga.shoppingapp.domain.useCase.GetAllProductsUseCase
import com.vaibhavranga.shoppingapp.domain.useCase.SignInWithEmailAndPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getAllProductsUseCase: GetAllProductsUseCase
) : ViewModel() {
    private val _createUserState = MutableStateFlow(CreateUserState())
    val createUserState = _createUserState.asStateFlow()

    private val _signInWithEmailAndPasswordState = MutableStateFlow(SignInWithEmailAndPasswordState())
    val signInWithEmailAndPasswordState = _signInWithEmailAndPasswordState.asStateFlow()

    private val _getAllCategoriesState = MutableStateFlow(GetAllCategoriesState())
    val getAllCategoriesState = _getAllCategoriesState.asStateFlow()

    private val _getAllProductsState = MutableStateFlow(GetAllProductsState())
    val getAllProductsState = _getAllProductsState.asStateFlow()

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

    fun clearCreateUserState() {
        _createUserState.value = CreateUserState()
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            signInWithEmailAndPasswordUseCase.signInWithEmailAndPassword(email = email, password = password).collect { response ->
                when (response) {
                    is ResultState.Error -> _signInWithEmailAndPasswordState.value = SignInWithEmailAndPasswordState(isLoading = false, error = response.error)
                    ResultState.Loading -> _signInWithEmailAndPasswordState.value = SignInWithEmailAndPasswordState(isLoading = true)
                    is ResultState.Success -> _signInWithEmailAndPasswordState.value = SignInWithEmailAndPasswordState(isLoading = false, data = response.data)
                }
            }
        }
    }

    fun clearSignInWithEmailAndPasswordState() {
        _signInWithEmailAndPasswordState.value = SignInWithEmailAndPasswordState()
    }

    fun getAllCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllCategoriesUseCase.getAllCategories().collect { response ->
                when (response) {
                    is ResultState.Error -> _getAllCategoriesState.value = GetAllCategoriesState(isLoading = false, error = response.error)
                    ResultState.Loading -> _getAllCategoriesState.value = GetAllCategoriesState(isLoading = true)
                    is ResultState.Success -> _getAllCategoriesState.value = GetAllCategoriesState(isLoading = false, data = response.data)
                }
            }
        }
    }

    fun clearGetAllCategoriesState() {
        _getAllCategoriesState.value = GetAllCategoriesState()
    }

    fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllProductsUseCase.getAllProducts().collect { response ->
                when (response) {
                    is ResultState.Error -> _getAllProductsState.value = GetAllProductsState(isLoading = false, error = response.error)
                    ResultState.Loading -> _getAllProductsState.value = GetAllProductsState(isLoading = true)
                    is ResultState.Success -> _getAllProductsState.value = GetAllProductsState(isLoading = false, data = response.data)
                }
            }
        }
    }

    fun clearGetAllProductsState() {
        _getAllProductsState.value = GetAllProductsState()
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

data class GetAllCategoriesState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: List<CategoryModel>? = null
)

data class GetAllProductsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: List<ProductModel>? = null
)