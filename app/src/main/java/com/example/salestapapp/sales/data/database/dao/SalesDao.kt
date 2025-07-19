package com.example.salestapapp.sales.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.salestapapp.sales.data.database.entities.SalesEntity

@Dao
interface SalesDao {

    @Query("SELECT *FROM Sales ORDER BY CreateDate DESC")
    suspend fun  getAllSales(): List<SalesEntity>

    @Query("SELECT *FROM Sales WHERE ID = :salesId")
    suspend fun  getSalesByID(salesId: Int): SalesEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)//aqui le digo que si hay uno igual que lo remplaze
    suspend fun insertAll(sales:List<SalesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)//aqui le digo que si hay uno igual que lo remplaze
    suspend fun insertOne(sales: SalesEntity):Long

    @Update
    suspend fun editSales(sales: SalesEntity):Int

    @Query("DELETE FROM Sales WHERE id = :salesId")
    suspend fun deleteSalesById(salesId: Int)

}