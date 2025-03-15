package com.vaibhavranga.shoppingapp.domain.useCase

import com.vaibhavranga.shoppingapp.common.ResultState
import com.vaibhavranga.shoppingapp.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteWishListItemUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(productId: String): Flow<ResultState<String>> =
        repository.deleteWishListItem(productId = productId)
}