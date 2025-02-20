package com.vaibhavranga.shoppingapp.domain.useCase

import com.vaibhavranga.shoppingapp.common.ResultState
import com.vaibhavranga.shoppingapp.domain.model.WishListModel
import com.vaibhavranga.shoppingapp.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllWishListItemsUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(): Flow<ResultState<List<WishListModel>>> =
        repository.getAllWishListItems()
}