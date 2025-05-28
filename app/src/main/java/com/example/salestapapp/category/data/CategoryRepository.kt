package com.example.salestapapp.category.data

import com.example.salestapapp.category.data.database.entities.CategoryEntity
import com.example.salestapapp.rom.CyberCoffeDatabase

class CategoryRepository(
    private var db: CyberCoffeDatabase
) {

    suspend fun insertCategory (category: CategoryEntity): Int {
        return db.categoryDao().insertOne(category).toInt()
    }

    suspend fun editCategory (category: CategoryEntity): Int{
        return db.categoryDao().editCategory(category)
    }

    suspend fun getCategoryById(categoryID: Int): CategoryEntity {
        return db.categoryDao().getCategoryByID(categoryID)
    }

    suspend fun deleteById (categoryID: Int):Boolean {
        return try {
            db.categoryDao().deleteCategoryById(categoryID)
            true
        }catch(e: Exception){
            false
        }

    }

    suspend fun getAllData (): List<CategoryEntity> {
        return db.categoryDao().getAllCategorys()
    }

}