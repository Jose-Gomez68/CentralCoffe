package com.example.salestapapp.supplier.data.domain.usecase

import com.example.salestapapp.supplier.data.database.entities.toDatabase
import com.example.salestapapp.supplier.data.domain.repository.SupplierRepository
import com.example.salestapapp.supplier.data.model.SuppliersModel

class EditSupplierUseCase(private val repository: SupplierRepository) {

    suspend fun invoke(supplier: SuppliersModel): Boolean {
        val updateSupplier = repository.editSupplier(supplier = supplier.toDatabase()) > 0
        if (updateSupplier){
            return true
        }else{
            throw Exception("Faild to update supplier into database")
        }
    }

}