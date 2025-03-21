package com.vaibhavranga.shoppingapp.domain.repository

import com.vaibhavranga.shoppingapp.common.ResultState
import com.vaibhavranga.shoppingapp.domain.model.CategoryModel
import com.vaibhavranga.shoppingapp.domain.model.ProductModel
import com.vaibhavranga.shoppingapp.domain.model.UserDataModel
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun registerUserWithEmailAndPassword(userData: UserDataModel): Flow<ResultState<String>>

    suspend fun signInWithEmailAndPassword(email: String, password: String): Flow<ResultState<String>>

    suspend fun getAllCategories(): Flow<ResultState<List<CategoryModel>>>

    suspend fun getAllProducts(): Flow<ResultState<List<ProductModel>>>

    suspend fun getAllProductsByCategory(categoryName: String): Flow<ResultState<List<ProductModel>>>

    suspend fun getProductById(productId: String): Flow<ResultState<ProductModel>>

    suspend fun addToWishList(productId: String): Flow<ResultState<String>>

    suspend fun getAllWishListItems(): Flow<ResultState<List<String>>>

    suspend fun deleteWishListItem(productId: String): Flow<ResultState<String>>

    suspend fun searchProduct(query: String): Flow<ResultState<List<ProductModel>>>

    suspend fun getFlashSaleProducts(): Flow<ResultState<List<ProductModel>>>

    suspend fun addProductToCart(productId: String): Flow<ResultState<String>>

    suspend fun getAllCartItems(): Flow<ResultState<List<String>>>

    suspend fun deleteCartItem(productId: String): Flow<ResultState<String>>
}