package com.example.salestapapp.category.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.salestapapp.category.data.database.entities.CategoryEntity

@Dao
interface CategoryDao {

    @Query("SELECT *FROM Category ORDER BY Name DESC")
    suspend fun  getAllCategorys(): List<CategoryEntity>

    @Query("SELECT *FROM Category WHERE ID = :categoryID")
    suspend fun  getCategoryByID(categoryID: Int): CategoryEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)//aqui le digo que si hay uno igual que lo remplaze
    suspend fun insertAll(categorys:List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)//aqui le digo que si hay uno igual que lo remplaze
    suspend fun insertOne(category: CategoryEntity):Long

    @Update
    suspend fun editCategory(category: CategoryEntity):Int

    @Query("DELETE FROM Category WHERE id = :categoryId")
    suspend fun deleteCategoryById(categoryId: Int)

}