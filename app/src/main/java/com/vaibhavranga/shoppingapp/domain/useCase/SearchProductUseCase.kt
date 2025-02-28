package com.vaibhavranga.shoppingapp.domain.useCase

import com.vaibhavranga.shoppingapp.common.ResultState
import com.vaibhavranga.shoppingapp.domain.model.ProductModel
import com.vaibhavranga.shoppingapp.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchProductUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(query: String): Flow<ResultState<List<ProductModel>>> =
        repository.searchProduct(query = query)
}