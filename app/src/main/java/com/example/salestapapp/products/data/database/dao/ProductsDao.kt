package com.example.salestapapp.products.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.salestapapp.products.data.database.entities.ProductsEntity

@Dao
interface ProductsDao {

    @Query("SELECT *FROM Products ORDER BY Name DESC")
    suspend fun  getAllProducts(): List<ProductsEntity>

    @Query("SELECT *FROM Products WHERE ID = :productId")
    suspend fun  getProductByID(productId: Int): ProductsEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)//aqui le digo que si hay uno igual que lo remplaze
    suspend fun insertAll(products:List<ProductsEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)//aqui le digo que si hay uno igual que lo remplaze
    suspend fun insertOne(product:ProductsEntity):Long

    @Update
    suspend fun editProduct(product:ProductsEntity):Int

    @Query("DELETE FROM Products WHERE id = :productId")
    suspend fun deleteProductById(productId: Int)

}