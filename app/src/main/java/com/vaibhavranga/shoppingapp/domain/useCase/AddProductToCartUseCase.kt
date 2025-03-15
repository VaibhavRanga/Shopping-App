package com.vaibhavranga.shoppingapp.domain.useCase

import com.vaibhavranga.shoppingapp.domain.repository.Repository
import javax.inject.Inject

class AddProductToCartUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(productId: String) =
        repository.addProductToCart(productId = productId)
}