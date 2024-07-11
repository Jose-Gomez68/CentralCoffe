package com.example.salestapapp.supplier.data.domain

import com.example.salestapapp.supplier.data.SupplierRepository
import com.example.salestapapp.supplier.data.model.SuppliersModel

class DeleteSupplierByIDUseCase(private val repository: SupplierRepository) {

    suspend operator fun invoke(supplier: SuppliersModel): Boolean {
        val deleteSupplier = repository.deleteByID(supplierID = supplier.id)

        if (deleteSupplier) {
            return true
        }else {
            return false
            throw Exception("Failed to insert supplier into to database")
        }
    }

}