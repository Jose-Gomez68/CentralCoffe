package com.example.salestapapp.supplier.data

import com.example.salestapapp.rom.CyberCoffeDatabase
import com.example.salestapapp.supplier.data.database.entities.SupplierEntity

class SupplierRepository(private var db: CyberCoffeDatabase) {
    
    suspend fun addSupplier(supplier: SupplierEntity): Int{
        return db.suppliersDao().addSupplier(supplier).toInt()
    }

}