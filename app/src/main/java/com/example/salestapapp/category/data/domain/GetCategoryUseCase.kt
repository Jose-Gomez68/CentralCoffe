package com.example.salestapapp.category.data.domain

import com.example.salestapapp.category.data.CategoryRepository
import com.example.salestapapp.category.data.model.CategoryModel
import com.example.salestapapp.category.data.model.toDomain

class GetCategoryUseCase(private val repository: CategoryRepository) {

    suspend operator fun invoke(): List<CategoryModel> {
        val categorys = repository.getAllData().map { it.toDomain() }

        return categorys.ifEmpty {
            emptyList<CategoryModel>()
        }
    }

}