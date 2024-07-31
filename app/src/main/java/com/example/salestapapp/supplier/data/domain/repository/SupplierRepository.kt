package com.example.salestapapp.supplier.data.domain.repository

import com.example.salestapapp.rom.CyberCoffeDatabase
import com.example.salestapapp.supplier.data.database.entities.SupplierEntity
import com.example.salestapapp.supplier.data.model.SuppliersModel

class SupplierRepository(private var db: CyberCoffeDatabase) {
    
    suspend fun addSupplier(supplier: SupplierEntity): Int{
        return db.suppliersDao().addSupplier(supplier).toInt()
    }

    suspend fun editSupplier(supplier: SupplierEntity): Int{
        return db.suppliersDao().editSupplier(supplier).toInt()
    }

    suspend fun getAllSuppliers(): List<SupplierEntity> {
        return db.suppliersDao().getAllSupplier()
    }

    suspend fun getSupplierByID(supplierID: Int): SupplierEntity {
        return db.suppliersDao().getSupplierByID(supplierID)
    }

    suspend fun deleteByID(supplierID: Int): Boolean {
        return try {
            db.suppliersDao().deleteSupplierByID(supplierID)
            true
        }catch (e: Exception){
            false
        }
    }

}