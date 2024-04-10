package com.example.salestapapp.supplier.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.salestapapp.supplier.data.database.entities.SupplierEntity

@Dao
interface SuppliersDao {

    @Query("SELECT *FROM Suppliers ORDER BY Name DESC")
    suspend fun  getAllSupplier(): List<SupplierEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)//aqui le digo que si hay uno igual que lo remplaze
    suspend fun addSupplier(suppliers:SupplierEntity):Long

}