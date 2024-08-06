package com.example.salestapapp.supplier.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.salestapapp.supplier.data.database.entities.SupplierEntity

@Dao
interface SuppliersDao {

    @Query("SELECT *FROM Suppliers ORDER BY Name DESC")
    suspend fun  getAllSupplier(): List<SupplierEntity>

    @Query("SELECT *FROM Suppliers WHERE id = :supplierID")
    suspend fun  getSupplierByID(supplierID: Int): SupplierEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)//aqui le digo que si hay uno igual que lo remplaze
    suspend fun addSupplier(suppliers:SupplierEntity):Long

    @Query("DELETE FROM Suppliers WHERE id = :supplierID")
    suspend fun deleteSupplierByID(supplierID: Int)

    @Update
    suspend fun editSupplier(suppliers: SupplierEntity):Int

}