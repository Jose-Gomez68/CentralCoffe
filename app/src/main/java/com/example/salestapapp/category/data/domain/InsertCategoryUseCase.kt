package com.example.salestapapp.category.data.domain

import com.example.salestapapp.category.data.CategoryRepository
import com.example.salestapapp.category.data.database.entities.toDatabase
import com.example.salestapapp.category.data.model.CategoryModel
import com.example.salestapapp.category.data.model.toDomain

class InsertCategoryUseCase(private val repository: CategoryRepository) {

    suspend operator fun invoke(category: CategoryModel): CategoryModel {

        val insetCategoryId = repository.insertCategory(category.toDatabase())
        val getInsetCategory = repository.getCategoryById(insetCategoryId)

        if (getInsetCategory != null){
            return getInsetCategory.toDomain()
        }else{
            throw Exception("Failed to Insert Category Into Database")
        }

    }

}