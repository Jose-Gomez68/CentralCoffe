package com.example.salestapapp.sales.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.salestapapp.sales.data.database.entities.SalesDetailEntity

@Dao
interface SalesDetailDao {

    @Query("SELECT *FROM SalesDetail ORDER BY ID DESC")
    suspend fun  getAllSalesDetails(): List<SalesDetailEntity>

    @Query("SELECT *FROM SalesDetail WHERE ID = :salesDetailId")
    suspend fun  getSaleDetailByID(salesDetailId: Int): SalesDetailEntity

    @Query("SELECT *FROM SalesDetail WHERE ID = :saleId")
    suspend fun  getSalesDetailsByID(saleId: Int): List<SalesDetailEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)//aqui le digo que si hay uno igual que lo remplaze
    suspend fun insertAllDetails(sales:List<SalesDetailEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)//aqui le digo que si hay uno igual que lo remplaze
    suspend fun insertOne(sales: SalesDetailEntity):Long

    @Update
    suspend fun editSalesDetails(sales: SalesDetailEntity):Int

    @Query("DELETE FROM SalesDetail WHERE id = :salesDetailId")
    suspend fun deleteSalesDetailsById(salesDetailId: Int)

}