package com.example.salestapapp.category.data.domain

import android.util.Log
import com.example.salestapapp.category.data.CategoryRepository
import com.example.salestapapp.category.data.database.entities.toDatabase
import com.example.salestapapp.category.data.model.CategoryModel
import com.example.salestapapp.category.data.model.toDomain

class EditCategoryUseCase(
    private val repository: CategoryRepository
) {

    suspend operator fun invoke(category: CategoryModel): CategoryModel {
        return try {
            val rowsUpdate = repository.editCategory(category.toDatabase())
            return if (rowsUpdate > 0) {
                repository.getCategoryById(categoryID = category.id)?.toDomain()
                    ?: throw Exception("Failed to retrieve updated category from database")
            } else {
                throw Exception("Failed to update product into database")
            }
        } catch (e: Exception){
            e.printStackTrace()
            // Imprimir el error con información adicional
            Log.e("EditCategoryUseCase", "Error update category", e)
            throw e // o puedes manejarlo de otra forma
        }
    }

}