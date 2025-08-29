package com.example.salestapapp.sales.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.salestapapp.reports.data.model.ProductosMasVendidosModel
import com.example.salestapapp.sales.data.database.entities.SalesDetailEntity

@Dao
interface SalesDetailDao {

    @Query("SELECT *FROM SalesDetail ORDER BY ID DESC")
    suspend fun  getAllSalesDetails(): List<SalesDetailEntity>

    @Query("SELECT *FROM SalesDetail WHERE ID = :salesDetailId")
    suspend fun  getSaleDetailByID(salesDetailId: Int): SalesDetailEntity

    @Query("SELECT *FROM SalesDetail WHERE saleId = :saleId")
    suspend fun  getSalesDetailsByID(saleId: Int): List<SalesDetailEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)//aqui le digo que si hay uno igual que lo remplaze
    suspend fun insertAllDetails(sales:List<SalesDetailEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)//aqui le digo que si hay uno igual que lo remplaze
    suspend fun insertOne(sales: SalesDetailEntity):Long

    @Update
    suspend fun editSalesDetails(sales: SalesDetailEntity):Int

    @Query("DELETE FROM SalesDetail WHERE id = :salesDetailId")
    suspend fun deleteSalesDetailsById(salesDetailId: Int)

    @Query("""
    SELECT 
        p.ID AS productId,
        p.Name AS productName,
        p.Category AS category,
        SUM(sd.Quantity) AS totalVendidos,
        SUM(sd.TotalPrice) AS totalIngresos
    FROM SalesDetail sd
    INNER JOIN Products p ON p.ID = sd.ProductID
    INNER JOIN Sales s ON s.ID = sd.SaleID
    WHERE s.CreateDate BETWEEN :startDate AND :endDate
    GROUP BY p.ID, p.Name, p.Category
    ORDER BY totalVendidos DESC
""")
    suspend fun getProductosMasVendidosBetweenDates(
        startDate: String, // Ej: "20/08/2025 00:00:00"
        endDate: String    // Ej: "20/08/2025 23:59:59"
    ): List<ProductosMasVendidosModel>

}