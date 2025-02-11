package com.vaibhavranga.shoppingapp.domain.useCase

import com.vaibhavranga.shoppingapp.domain.repository.Repository
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(private val repository: Repository) {
    suspend fun getAllCategories() = repository.getAllCategories()
}