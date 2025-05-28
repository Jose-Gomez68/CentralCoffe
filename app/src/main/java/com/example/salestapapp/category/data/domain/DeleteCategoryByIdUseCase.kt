package com.example.salestapapp.category.data.domain

import com.example.salestapapp.category.data.CategoryRepository
import com.example.salestapapp.category.data.model.CategoryModel

class DeleteCategoryByIdUseCase(private val repository: CategoryRepository) {

    suspend operator fun invoke(category: CategoryModel): Boolean {
        val deleteCategory = repository.deleteById(categoryID = category.id)

        if (deleteCategory) {
            return true
        }else {
            return false
            throw Exception("Failed to delete category into database")
        }
    }

}