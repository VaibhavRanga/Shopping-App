package com.vaibhavranga.shoppingapp.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.vaibhavranga.shoppingapp.common.ResultState
import com.vaibhavranga.shoppingapp.domain.model.CategoryModel
import com.vaibhavranga.shoppingapp.domain.model.ProductModel
import com.vaibhavranga.shoppingapp.domain.model.UserDataModel
import com.vaibhavranga.shoppingapp.domain.model.WishListModel
import com.vaibhavranga.shoppingapp.domain.useCase.AddToWishListUseCase
import com.vaibhavranga.shoppingapp.domain.useCase.CreateUserUseCase
import com.vaibhavranga.shoppingapp.domain.useCase.GetAllCategoriesUseCase
import com.vaibhavranga.shoppingapp.domain.useCase.GetAllProductsByCategoryUseCase
import com.vaibhavranga.shoppingapp.domain.useCase.GetAllProductsUseCase
import com.vaibhavranga.shoppingapp.domain.useCase.GetAllWishListItemsUseCase
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
//    private val deleteWishListItemUseCase: DeleteWishListItemUseCase,
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

    private val _getAllProductsInWishListState = MutableStateFlow(GetAllProductsInWishListState())
    val getAllProductsInWishListState = _getAllProductsInWishListState.asStateFlow()

//    private val _deleteWishListItemState = MutableStateFlow(DeleteWishListItemState())
//    val deleteWishListItemState = _deleteWishListItemState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchProductState = MutableStateFlow(SearchProductState())
    val searchProductState = _searchProductState.asStateFlow()

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

    fun addToWishList(wishListModel: WishListModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addToWishListUseCase.invoke(wishListModel = wishListModel).collect { response ->
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

//                        if (wishListItems.isEmpty()) {
//                            _getAllWishListItemsState.value =
//                                GetAllWishListItemsState(isLoading = false, data = emptyList())
//                            return@collect
//                        }
//
//                        val productsList = wishListItems.mapNotNull { wishListItem ->
//                            getProductByIdUseCase.invoke(wishListItem.productId)
//                                .filterIsInstance<ResultState.Success<ProductModel>>()
//                                .map { it.data }
//                                .firstOrNull()
//                        }

                        _getAllWishListItemsState.value =
                            GetAllWishListItemsState(isLoading = false, data = response.data)
                    }
                }
            }
        }
    }

    fun getAllProductsInWishList(
        productIdListInWishList: List<WishListModel>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _getAllProductsInWishListState.value = GetAllProductsInWishListState(isLoading = true)
            val productsList = productIdListInWishList.map { wishListItem ->
                getProductByIdUseCase.invoke(wishListItem.productId)
                    .filterIsInstance<ResultState.Success<ProductModel>>()
                    .map { it.data }
                    .firstOrNull()
            }
            _getAllProductsInWishListState.value =
                GetAllProductsInWishListState(isLoading = false, data = productsList)
        }
    }

//    fun deleteWishListItem(wishId: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            deleteWishListItemUseCase.invoke(wishId = wishId).collect { response ->
//                when (response) {
//                    is ResultState.Error -> _deleteWishListItemState.value =
//                        DeleteWishListItemState(isLoading = false, error = response.error)
//
//                    ResultState.Loading -> _deleteWishListItemState.value =
//                        DeleteWishListItemState(isLoading = true)
//
//                    is ResultState.Success -> _deleteWishListItemState.value =
//                        DeleteWishListItemState(isLoading = false, data = response.data)
//                }
//            }
//        }
//    }

    @OptIn(FlowPreview::class)
    private fun searchProduct() {
        viewModelScope.launch(Dispatchers.IO) {
            searchProductUseCase.invoke(query = searchQuery.value).debounce(500L).collect { response ->
                when (response) {
                    is ResultState.Error -> _searchProductState.value = SearchProductState(isLoading = false, error = response.error)
                    ResultState.Loading -> _searchProductState.value = SearchProductState(isLoading = true)
                    is ResultState.Success -> _searchProductState.value = SearchProductState(isLoading = false, data = response.data)
                }
            }
        }
    }

    fun clearGetAllWishListItemsState() {
        _getAllWishListItemsState.value = GetAllWishListItemsState()
    }

    fun signOut() {
        firebaseAuth.signOut()
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
    val data: List<WishListModel>? = null
)

data class GetAllProductsInWishListState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: List<ProductModel?>? = null
)

data class SearchProductState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: List<ProductModel>? = null
)

//data class DeleteWishListItemState(
//    val isLoading: Boolean = false,
//    val error: String? = null,
//    val data: String? = null
//)