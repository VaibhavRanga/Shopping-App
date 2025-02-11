package com.vaibhavranga.shoppingapp.domain.useCase

import com.vaibhavranga.shoppingapp.domain.repository.Repository
import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(private val repository: Repository) {
    suspend fun getAllProducts() = repository.getAllProducts()
}