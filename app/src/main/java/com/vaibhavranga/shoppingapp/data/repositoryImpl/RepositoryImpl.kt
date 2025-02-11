package com.vaibhavranga.shoppingapp.data.repositoryImpl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vaibhavranga.shoppingapp.common.CATEGORY_PATH
import com.vaibhavranga.shoppingapp.common.PRODUCT_PATH
import com.vaibhavranga.shoppingapp.common.ResultState
import com.vaibhavranga.shoppingapp.common.USER_PATH
import com.vaibhavranga.shoppingapp.domain.model.CategoryModel
import com.vaibhavranga.shoppingapp.domain.model.ProductModel
import com.vaibhavranga.shoppingapp.domain.model.UserDataModel
import com.vaibhavranga.shoppingapp.domain.repository.Repository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : Repository {
    override suspend fun registerUserWithEmailAndPassword(
        userData: UserDataModel
    ): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        try {
            firebaseAuth.createUserWithEmailAndPassword(userData.email, userData.password)
                .addOnSuccessListener { authResult ->
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
                        document.toObject(ProductModel::class.java)
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
}