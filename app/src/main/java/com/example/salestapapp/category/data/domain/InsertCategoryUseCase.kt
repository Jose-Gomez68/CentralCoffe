package com.example.salestapapp.category.data.domain

import android.util.Log
import com.example.salestapapp.category.data.CategoryRepository
import com.example.salestapapp.category.data.database.entities.toDatabase
import com.example.salestapapp.category.data.model.CategoryModel
import com.example.salestapapp.category.data.model.toDomain

class InsertCategoryUseCase(private val repository: CategoryRepository) {

    suspend operator fun invoke(category: CategoryModel): CategoryModel {

        return try {
            val insetCategoryId = repository.insertCategory(category.toDatabase())
            val getInsetCategory = repository.getCategoryById(insetCategoryId)

            if (getInsetCategory != null){
                return getInsetCategory.toDomain()
            } else {
                throw Exception("Failed to insert category into database")
            }

        }catch (e: Exception) {
            e.printStackTrace()
            // Imprimir el error con información adicional
            Log.e("InsertCategoryUseCase", "Error inserting category", e)
            throw e // o puedes manejarlo de otra forma
        }

    }

}