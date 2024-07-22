package com.example.salestapapp.supplier.data.domain

import com.example.salestapapp.supplier.data.SupplierRepository
import com.example.salestapapp.supplier.data.database.entities.toDatabase
import com.example.salestapapp.supplier.data.model.SuppliersModel
import com.example.salestapapp.supplier.data.model.toDomain

class SaveSupplierUseCase(private val repository: SupplierRepository) {

    suspend fun invoke(supplier: SuppliersModel): SuppliersModel {
        val supplierID = repository.addSupplier(supplier = supplier.toDatabase())
        val getNewSupplier = repository.getSupplierByID(supplierID)

        if (getNewSupplier != null){
            return getNewSupplier.toDomain()
        }else{
            throw Exception("Faild to insert supplier into database")
        }
    }

}