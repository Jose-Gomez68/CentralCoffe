package com.example.salestapapp.category.data.domain

import android.database.sqlite.SQLiteException
import android.util.Log
import com.example.salestapapp.category.data.CategoryRepository
import com.example.salestapapp.category.data.model.CategoryModel
import com.example.salestapapp.category.data.model.toDomain

class GetCategoryByIdUseCase(
    private val repository: CategoryRepository
) {

    suspend operator fun invoke(categoryID: Int): CategoryModel {
        return try {
            val result = repository.getCategoryById(categoryID)
            result.toDomain()
        } catch (e: SQLiteException) {
            Log.e("GetCategoryByIdUseCase", "Error de base de datos: ${e.message}", e)
            e.printStackTrace()
            // Devolver un modelo vacío como fallback
            CategoryModel(
                id = 0,
                name = "",
                createDate = "",
                updateDate = ""
            )
        } catch (e: Exception) {
            Log.e("GetCategoryByIdUseCase", "Error inesperado: ${e.message}", e)
            e.printStackTrace()
            CategoryModel(
                id = 0,
                name = "",
                createDate = "",
                updateDate = ""
            )
        }
    }

}