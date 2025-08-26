package com.example.salestapapp.sales.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.salestapapp.reports.data.model.CashCutModel
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

    @Query("""
    SELECT 
        COALESCE(SUM(CASE WHEN PaymentMethod = 'TARJETA CRÉDITO' THEN Total ELSE 0 END), 0) AS ttCredito,
        COALESCE(SUM(CASE WHEN PaymentMethod = 'TARJETA DÉBITO' THEN Total ELSE 0 END), 0) AS ttDebito,
        COALESCE(SUM(CASE WHEN PaymentMethod = 'TRANSFERENCIA' THEN Total ELSE 0 END), 0) AS ttTransfer,
        COALESCE(SUM(CASE WHEN PaymentMethod = 'EFECTIVO' THEN Total ELSE 0 END), 0) AS ttEfective
    FROM Sales
    WHERE substr(CreateDate, 1, 10) = :date
      AND substr(CreateDate, 12, 8) BETWEEN :startTime AND :endTime
""")
    suspend fun  getCashCut(date: String, startTime: String, endTime: String): CashCutModel

    @Query("""
    SELECT * FROM Sales
    WHERE substr(CreateDate, 1, 10) = :date
    AND substr(CreateDate, 12, 8) BETWEEN :startTime AND :endTime
""")
    suspend fun getCashCutSales(date: String, startTime: String, endTime: String): List<SalesEntity>


    @Query("""
    SELECT * FROM Sales 
    WHERE substr(CreateDate, 1, 10) = :date
    ORDER BY CreateDate DESC
""")
    suspend fun getSalesByDate(date: String): List<SalesEntity>

    @Query("""
    SELECT * FROM Sales
    WHERE CreateDate BETWEEN :startDate AND :endDate
    ORDER BY CreateDate DESC
""")
    suspend fun getSalesBetweenDates(
        startDate: String, // "20/08/2025 00:00:00"
        endDate: String    // "20/08/2025 23:59:59"
    ): List<SalesEntity>

}