package com.vaibhavranga.shoppingapp.data.repositoryImpl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.vaibhavranga.shoppingapp.common.CART_PATH
import com.vaibhavranga.shoppingapp.common.CART_USER_ID_PATH
import com.vaibhavranga.shoppingapp.common.CATEGORY_PATH
import com.vaibhavranga.shoppingapp.common.PRODUCT_PATH
import com.vaibhavranga.shoppingapp.common.ResultState
import com.vaibhavranga.shoppingapp.common.USER_FCM_TOKEN_PATH
import com.vaibhavranga.shoppingapp.common.USER_PATH
import com.vaibhavranga.shoppingapp.common.WISHLISTS_PATH
import com.vaibhavranga.shoppingapp.common.WISHLIST_USER_ID_PATH
import com.vaibhavranga.shoppingapp.domain.model.CartItemModel
import com.vaibhavranga.shoppingapp.domain.model.CategoryModel
import com.vaibhavranga.shoppingapp.domain.model.ProductModel
import com.vaibhavranga.shoppingapp.domain.model.UserDataModel
import com.vaibhavranga.shoppingapp.domain.model.WishListModel
import com.vaibhavranga.shoppingapp.domain.repository.Repository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseMessaging: FirebaseMessaging
) : Repository {
    override suspend fun registerUserWithEmailAndPassword(
        userData: UserDataModel
    ): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        try {
            firebaseAuth.createUserWithEmailAndPassword(userData.email, userData.password)
                .addOnSuccessListener { authResult ->
                    updateFCMToken(userId = authResult.user!!.uid)
                    firebaseFirestore.collection(USER_PATH).document(
                        authResult.user!!.uid
                    ).set(userData)
                        .addOnSuccessListener {
                            trySend(ResultState.Success(data = "User created successfully"))
                        }
                        .addOnFailureListener {
                            trySend(ResultState.Error(error = it.message.toString()))
                        }
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(error = it.message.toString()))
                }
        } catch (e: Exception) {
            trySend(ResultState.Error(error = e.message.toString()))
        }

        awaitClose {
            close()
        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    updateFCMToken(userId = it.user!!.uid)
                    trySend(ResultState.Success(data = "Sign in successful"))
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(error = it.message.toString()))
                }
        } catch (e: Exception) {
            trySend(ResultState.Error(error = e.message.toString()))
        }

        awaitClose {
            close()
        }
    }

    override suspend fun getAllCategories(): Flow<ResultState<List<CategoryModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        try {
            firebaseFirestore.collection(CATEGORY_PATH).get()
                .addOnSuccessListener {
                    val categories = it.documents.mapNotNull { document ->
                        document.toObject(CategoryModel::class.java)
                    }
                    trySend(ResultState.Success(data = categories))
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(error = it.message.toString()))
                }
        } catch (e: Exception) {
            trySend(ResultState.Error(error = e.message.toString()))
        }

        awaitClose {
            close()
        }
    }

    override suspend fun getAllProducts(): Flow<ResultState<List<ProductModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        try {
            firebaseFirestore.collection(PRODUCT_PATH).get()
                .addOnSuccessListener {
                    val products = it.documents.mapNotNull { document ->
                        document.toObject(ProductModel::class.java)?.apply {
                            productId = document.id
                        }
                    }
                    trySend(ResultState.Success(data = products))
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(error = it.message.toString()))
                }
        } catch (e: Exception) {
            trySend(ResultState.Error(error = e.message.toString()))
        }

        awaitClose {
            close()
        }
    }

    override suspend fun getAllProductsByCategory(categoryName: String): Flow<ResultState<List<ProductModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)

            try {
                firebaseFirestore.collection(PRODUCT_PATH).whereEqualTo("category", categoryName)
                    .get()
                    .addOnSuccessListener {
                        val products = it.documents.mapNotNull { document ->
                            document.toObject(ProductModel::class.java)?.apply {
                                productId = document.id
                            }
                        }
                        trySend(ResultState.Success(data = products))
                    }
                    .addOnFailureListener {
                        trySend(ResultState.Error(error = it.message.toString()))
                    }
            } catch (e: Exception) {
                trySend(ResultState.Error(error = e.message.toString()))
            }

            awaitClose {
                close()
            }
        }

    override suspend fun getProductById(productId: String): Flow<ResultState<ProductModel>> =
        callbackFlow {
            trySend(ResultState.Loading)

            try {
                firebaseFirestore.collection(PRODUCT_PATH).document(productId).get()
                    .addOnSuccessListener { document ->
                        val product = document.toObject(ProductModel::class.java)
                        product?.productId = productId
                        product?.let {
                            trySend(ResultState.Success(data = it))
                        }
                    }
                    .addOnFailureListener {
                        trySend(ResultState.Error(error = it.message.toString()))
                    }
            } catch (e: Exception) {
                trySend(ResultState.Error(error = e.message.toString()))
            }

            awaitClose {
                close()
            }
        }

    override suspend fun addToWishList(wishListModel: WishListModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            try {
                firebaseFirestore
                    .collection(WISHLISTS_PATH)
                    .document(firebaseAuth.currentUser!!.uid)
                    .collection(WISHLIST_USER_ID_PATH)
                    .add(wishListModel)
                    .addOnSuccessListener {
                        trySend(ResultState.Success(data = "Added to wishlist"))
                    }
                    .addOnFailureListener {
                        trySend(ResultState.Error(error = it.message.toString()))
                    }
            } catch (e: Exception) {
                trySend(ResultState.Error(error = e.message.toString()))
            }

            awaitClose {
                close()
            }
        }

    override suspend fun getAllWishListItems(): Flow<ResultState<List<WishListModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)

            try {
                firebaseFirestore
                    .collection(WISHLISTS_PATH)
                    .document(firebaseAuth.currentUser!!.uid)
                    .collection(WISHLIST_USER_ID_PATH)
                    .get()
                    .addOnSuccessListener {
                        val wishListItemsList = it.documents.mapNotNull { document ->
                            document.toObject(WishListModel::class.java)?.apply {
                                wishId = document.id
                            }
                        }
                        trySend(ResultState.Success(data = wishListItemsList))
                    }
                    .addOnFailureListener {
                        trySend(ResultState.Error(error = it.message.toString()))
                    }
            } catch (e: Exception) {
                trySend(ResultState.Error(error = e.message.toString()))
            }

            awaitClose {
                close()
            }
        }

    override suspend fun searchProduct(query: String): Flow<ResultState<List<ProductModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        try {
            firebaseFirestore
                .collection(PRODUCT_PATH)
                .orderBy("name")
                .startAt(query)
                .get()
                .addOnSuccessListener {
                    val products = it.documents.mapNotNull { document ->
                        document.toObject(ProductModel::class.java)?.apply {
                            productId = document.id
                        }
                    }
                    trySend(ResultState.Success(data = products))
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(error = it.message.toString()))
                }
        } catch (e: Exception) {
            trySend(ResultState.Error(error = e.message.toString()))
        }

        awaitClose {
            close()
        }
    }

    override suspend fun getFlashSaleProducts(): Flow<ResultState<List<ProductModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        try {
            firebaseFirestore
                .collection(PRODUCT_PATH)
                .limit(8)
                .get()
                .addOnSuccessListener {
                    val products = it.documents.mapNotNull { document ->
                        document.toObject(ProductModel::class.java)?.apply {
                            productId = document.id
                        }
                    }
                    trySend(ResultState.Success(data = products))
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(error = it.message.toString()))
                }
        } catch (e: Exception) {
            trySend(ResultState.Error(error = e.message.toString()))
        }

        awaitClose {
            close()
        }
    }

    override suspend fun addProductToCart(cartItemModel: CartItemModel): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        try {
            firebaseFirestore
                .collection(CART_PATH)
                .document(firebaseAuth.currentUser!!.uid)
                .collection(CART_USER_ID_PATH)
                .add(cartItemModel)
                .addOnSuccessListener {
                    trySend(ResultState.Success(data = "Item added to cart"))
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(error = it.message.toString()))
                }
        } catch (e: Exception) {
            trySend(ResultState.Error(error = e.message.toString()))
        }

        awaitClose {
            close()
        }
    }

//    override suspend fun deleteWishListItem(wishId: String): Flow<ResultState<String>> = callbackFlow {
//        trySend(ResultState.Loading)
//
//        try {
//            firebaseFirestore
//                .collection(WISHLISTS_PATH)
//                .document(firebaseAuth.currentUser!!.uid)
//                .collection(WISHLIST_USER_ID_PATH)
//                .document(wishId)
//                .delete()
//                .addOnSuccessListener {
//                    trySend(ResultState.Success(data = "Wish deleted successfully"))
//                }
//                .addOnFailureListener {
//                    trySend(ResultState.Error(error = it.message.toString()))
//                }
//        } catch (e: Exception) {
//            trySend(ResultState.Error(error = e.message.toString()))
//        }
//
//        awaitClose {
//            close()
//        }
//    }

    private fun updateFCMToken(userId: String) {
        firebaseMessaging
            .token
            .addOnSuccessListener {
                val token = it
                
                firebaseFirestore
                    .collection(USER_FCM_TOKEN_PATH)
                    .document(userId)
                    .set(
                        mapOf("token" to token)
                    )
            }
    }
}