package com.vaibhavranga.shoppingapp.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.vaibhavranga.shoppingapp.common.ResultState
import com.vaibhavranga.shoppingapp.domain.model.CategoryModel
import com.vaibhavranga.shoppingapp.domain.model.ProductModel
import com.vaibhavranga.shoppingapp.domain.model.UserDataModel
import com.vaibhavranga.shoppingapp.domain.useCase.AddProductToCartUseCase
import com.vaibhavranga.shoppingapp.domain.useCase.AddToWishListUseCase
import com.vaibhavranga.shoppingapp.domain.useCase.CreateUserUseCase
import com.vaibhavranga.shoppingapp.domain.useCase.DeleteCartItemUseCase
import com.vaibhavranga.shoppingapp.domain.useCase.DeleteWishListItemUseCase
import com.vaibhavranga.shoppingapp.domain.useCase.GetAllCartItemsUseCase
import com.vaibhavranga.shoppingapp.domain.useCase.GetAllCategoriesUseCase
import com.vaibhavranga.shoppingapp.domain.useCase.GetAllProductsByCategoryUseCase
import com.vaibhavranga.shoppingapp.domain.useCase.GetAllProductsUseCase
import com.vaibhavranga.shoppingapp.domain.useCase.GetAllWishListItemsUseCase
import com.vaibhavranga.shoppingapp.domain.useCase.GetFlashSaleProductsUseCase
import com.vaibhavranga.shoppingapp.domain.useCase.GetProductByIdUseCase
import com.vaibhavranga.shoppingapp.domain.useCase.SearchProductUseCase
import com.vaibhavranga.shoppingapp.domain.useCase.SignInWithEmailAndPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val getAllProductsByCategoryUseCase: GetAllProductsByCategoryUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val addToWishListUseCase: AddToWishListUseCase,
    private val getAllWishListItemsUseCase: GetAllWishListItemsUseCase,
    private val searchProductUseCase: SearchProductUseCase,
    private val getFlashSaleProductsUseCase: GetFlashSaleProductsUseCase,
    private val addProductToCartUseCase: AddProductToCartUseCase,
    private val deleteWishListItemUseCase: DeleteWishListItemUseCase,
    private val getAllCartItemsUseCase: GetAllCartItemsUseCase,
    private val deleteCartItemUseCase: DeleteCartItemUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {
    private val _createUserState = MutableStateFlow(CreateUserState())
    val createUserState = _createUserState.asStateFlow()

    private val _signInWithEmailAndPasswordState =
        MutableStateFlow(SignInWithEmailAndPasswordState())
    val signInWithEmailAndPasswordState = _signInWithEmailAndPasswordState.asStateFlow()

    private val _getAllCategoriesState = MutableStateFlow(GetAllCategoriesState())
    val getAllCategoriesState = _getAllCategoriesState.asStateFlow()

    private val _getAllProductsState = MutableStateFlow(GetAllProductsState())
    val getAllProductsState = _getAllProductsState.asStateFlow()

    private val _getAllProductsByCategoryState = MutableStateFlow(GetAllProductsByCategory())
    val getAllProductsByCategoryState = _getAllProductsByCategoryState.asStateFlow()

    private val _getProductByIdState = MutableStateFlow(GetProductByIdState())
    val getProductByIdState = _getProductByIdState.asStateFlow()

    private val _addToWishListState = MutableStateFlow(AddToWishListState())
    val addToWishListState = _addToWishListState.asStateFlow()

    private val _getAllWishListItemsState = MutableStateFlow(GetAllWishListItemsState())
    val getAllWishListItemsState = _getAllWishListItemsState.asStateFlow()

    private val _getFlashSaleProductsState = MutableStateFlow(GetFlashSaleProductsState())
    val getFlashSaleProductsState = _getFlashSaleProductsState.asStateFlow()

    private val _deleteWishListItemState = MutableStateFlow(DeleteWishListItemState())
    val deleteWishListItemState = _deleteWishListItemState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchProductState = MutableStateFlow(SearchProductState())
    val searchProductState = _searchProductState.asStateFlow()

    private val _addProductToCartState = MutableStateFlow(AddProductToCartState())
    val addProductToCartState = _addProductToCartState.asStateFlow()

    private val _getAllProductsInCartState = MutableStateFlow(GetAllProductsInCartState())
    val getAllProductsInCartState = _getAllProductsInCartState.asStateFlow()

    private val _deleteCartItemState = MutableStateFlow(DeleteCartItemState())
    val deleteCartItemState = _deleteCartItemState.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        searchProduct()
    }

    fun createUser(userData: UserDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            createUserUseCase.createUserUseCase(userData = userData).collect { response ->
                when (response) {
                    is ResultState.Error -> _createUserState.value =
                        CreateUserState(isLoading = false, error = response.error)

                    ResultState.Loading -> _createUserState.value =
                        CreateUserState(isLoading = true)

                    is ResultState.Success -> _createUserState.value =
                        CreateUserState(isLoading = false, data = response.data)
                }
            }
        }
    }

    fun clearCreateUserState() {
        _createUserState.value = CreateUserState()
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            signInWithEmailAndPasswordUseCase.signInWithEmailAndPassword(
                email = email,
                password = password
            ).collect { response ->
                when (response) {
                    is ResultState.Error -> _signInWithEmailAndPasswordState.value =
                        SignInWithEmailAndPasswordState(isLoading = false, error = response.error)

                    ResultState.Loading -> _signInWithEmailAndPasswordState.value =
                        SignInWithEmailAndPasswordState(isLoading = true)

                    is ResultState.Success -> _signInWithEmailAndPasswordState.value =
                        SignInWithEmailAndPasswordState(isLoading = false, data = response.data)
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
                    is ResultState.Error -> _getAllCategoriesState.value =
                        GetAllCategoriesState(isLoading = false, error = response.error)

                    ResultState.Loading -> _getAllCategoriesState.value =
                        GetAllCategoriesState(isLoading = true)

                    is ResultState.Success -> _getAllCategoriesState.value =
                        GetAllCategoriesState(isLoading = false, data = response.data)
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
                    is ResultState.Error -> _getAllProductsState.value =
                        GetAllProductsState(isLoading = false, error = response.error)

                    ResultState.Loading -> _getAllProductsState.value =
                        GetAllProductsState(isLoading = true)

                    is ResultState.Success -> _getAllProductsState.value =
                        GetAllProductsState(isLoading = false, data = response.data)
                }
            }
        }
    }

    fun clearGetAllProductsState() {
        _getAllProductsState.value = GetAllProductsState()
    }

    fun getAllProductsByCategory(categoryName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getAllProductsByCategoryUseCase.invoke(categoryName = categoryName)
                .collect { response ->
                    when (response) {
                        is ResultState.Error -> _getAllProductsByCategoryState.value =
                            GetAllProductsByCategory(isLoading = false, error = response.error)

                        ResultState.Loading -> _getAllProductsByCategoryState.value =
                            GetAllProductsByCategory(isLoading = true)

                        is ResultState.Success -> {
                            _getAllProductsByCategoryState.value =
                                GetAllProductsByCategory(isLoading = false, data = response.data)
                        }
                    }
                }
        }
    }

    fun clearGetAllProductsByCategoryState() {
        _getAllProductsByCategoryState.value = GetAllProductsByCategory()
    }

    fun getProductById(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getProductByIdUseCase.invoke(productId = productId).collect { response ->
                when (response) {
                    is ResultState.Error -> _getProductByIdState.value =
                        GetProductByIdState(isLoading = false, error = response.error)

                    ResultState.Loading -> _getProductByIdState.value =
                        GetProductByIdState(isLoading = true)

                    is ResultState.Success -> _getProductByIdState.value =
                        GetProductByIdState(isLoading = false, data = response.data)
                }
            }
        }
    }

    fun clearGetProductByIdState() {
        _getProductByIdState.value = GetProductByIdState()
    }

    fun addToWishList(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            addToWishListUseCase.invoke(productId = productId).collect { response ->
                when (response) {
                    is ResultState.Error -> _addToWishListState.value =
                        AddToWishListState(isLoading = false, error = response.error)

                    ResultState.Loading -> _addToWishListState.value =
                        AddToWishListState(isLoading = true)

                    is ResultState.Success -> _addToWishListState.value =
                        AddToWishListState(isLoading = false, data = response.data)
                }
            }
        }
    }

    fun clearAddToWishListState() {
        _addToWishListState.value = AddToWishListState()
    }

    fun getAllWishListItems() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllWishListItemsUseCase.invoke().collect { response ->
                when (response) {
                    is ResultState.Error -> _getAllWishListItemsState.value =
                        GetAllWishListItemsState(isLoading = false, error = response.error)

                    ResultState.Loading -> _getAllWishListItemsState.value =
                        GetAllWishListItemsState(isLoading = true)

                    is ResultState.Success -> {
                        val products = response.data.map { productId ->
                            getProductByIdUseCase.invoke(productId = productId)
                                .filterIsInstance<ResultState.Success<ProductModel>>()
                                .map { it.data }
                                .firstOrNull()!!
                        }
                        _getAllWishListItemsState.value =
                            GetAllWishListItemsState(isLoading = false, data = products)
                    }
                }
            }
        }
    }

    fun clearGetAllWishListItemsState() {
        _getAllWishListItemsState.value = GetAllWishListItemsState()
    }

    fun deleteWishListItem(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteWishListItemUseCase.invoke(productId = productId).collect { response ->
                when (response) {
                    is ResultState.Error -> _deleteWishListItemState.value =
                        DeleteWishListItemState(isLoading = false, error = response.error)

                    ResultState.Loading -> _deleteWishListItemState.value =
                        DeleteWishListItemState(isLoading = true)

                    is ResultState.Success -> _deleteWishListItemState.value =
                        DeleteWishListItemState(isLoading = false, data = response.data)
                }
            }
        }
    }

    fun clearDeleteWishListItemState() {
        _deleteWishListItemState.value = DeleteWishListItemState()
    }

    @OptIn(FlowPreview::class)
    private fun searchProduct() {
        viewModelScope.launch(Dispatchers.IO) {
            searchProductUseCase
                .invoke(query = searchQuery.value)
                .debounce(500L)
                .collect { response ->
                    when (response) {
                        is ResultState.Error -> _searchProductState.value =
                            SearchProductState(isLoading = false, error = response.error)

                        ResultState.Loading -> _searchProductState.value =
                            SearchProductState(isLoading = true)

                        is ResultState.Success -> _searchProductState.value =
                            SearchProductState(isLoading = false, data = response.data)
                    }
                }
        }
    }

    fun clearSearchProductState() {
        _searchProductState.value = SearchProductState()
    }

    fun getFlashSaleProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            getFlashSaleProductsUseCase.invoke().collect { response ->
                when (response) {
                    is ResultState.Error -> _getFlashSaleProductsState.value =
                        GetFlashSaleProductsState(isLoading = false, error = response.error)

                    ResultState.Loading -> _getFlashSaleProductsState.value =
                        GetFlashSaleProductsState(isLoading = true)

                    is ResultState.Success -> _getFlashSaleProductsState.value =
                        GetFlashSaleProductsState(isLoading = false, data = response.data)
                }
            }
        }
    }

    fun clearGetFlashSaleProductsState() {
        _getFlashSaleProductsState.value = GetFlashSaleProductsState()
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun addProductToCart(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            addProductToCartUseCase.invoke(productId = productId).collect { response ->
                when (response) {
                    is ResultState.Error -> _addProductToCartState.value =
                        AddProductToCartState(isLoading = false, error = response.error)

                    ResultState.Loading -> _addProductToCartState.value =
                        AddProductToCartState(isLoading = true)

                    is ResultState.Success -> _addProductToCartState.value =
                        AddProductToCartState(isLoading = false, data = response.data)
                }
            }
        }
    }

    fun clearAddProductToCartState() {
        _addProductToCartState.value = AddProductToCartState()
    }

    fun getAllCartItems() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllCartItemsUseCase.invoke().collect { response ->
                when (response) {
                    is ResultState.Error -> _getAllProductsInCartState.value = GetAllProductsInCartState(isLoading = false, error = response.error)
                    ResultState.Loading -> _getAllProductsInCartState.value = GetAllProductsInCartState(isLoading = true)
                    is ResultState.Success -> {
                        val productsList = response.data.map { productId ->
                            getProductByIdUseCase.invoke(productId = productId)
                                .filterIsInstance<ResultState.Success<ProductModel>>()
                                .map { it.data }
                                .firstOrNull()!!
                        }
                        _getAllProductsInCartState.value = GetAllProductsInCartState(isLoading = false, data = productsList)
                    }
                }
            }
        }
    }

    fun clearGetAllCartItemsState() {
        _getAllProductsInCartState.value = GetAllProductsInCartState()
    }

    fun deleteCartItem(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteCartItemUseCase.invoke(productId = productId).collect { response ->
                when (response) {
                    is ResultState.Error -> _deleteCartItemState.value = DeleteCartItemState(isLoading = false, error = response.error)
                    ResultState.Loading -> _deleteCartItemState.value = DeleteCartItemState(isLoading = true)
                    is ResultState.Success -> _deleteCartItemState.value = DeleteCartItemState(isLoading = false, data = response.data)
                }
            }
        }
    }

    fun clearDeleteCartItemState() {
        _deleteCartItemState.value = DeleteCartItemState()
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

data class GetAllProductsByCategory(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: List<ProductModel>? = null
)

data class GetProductByIdState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: ProductModel? = null
)

data class AddToWishListState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: String? = null
)

data class GetAllWishListItemsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: List<ProductModel>? = null
)

data class SearchProductState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: List<ProductModel>? = null
)

data class DeleteWishListItemState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: String? = null
)

data class GetFlashSaleProductsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: List<ProductModel>? = null
)

data class AddProductToCartState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: String? = null
)

data class GetAllProductsInCartState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: List<ProductModel>? = null
)

data class DeleteCartItemState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: String? = null
)