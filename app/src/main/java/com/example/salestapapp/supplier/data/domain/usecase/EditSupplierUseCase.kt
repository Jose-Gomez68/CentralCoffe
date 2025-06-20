package com.example.salestapapp.supplier.data.domain.usecase

import android.util.Log
import com.example.salestapapp.supplier.data.database.entities.toDatabase
import com.example.salestapapp.supplier.data.domain.repository.SupplierRepository
import com.example.salestapapp.supplier.data.model.SuppliersModel
import com.example.salestapapp.supplier.data.model.toDomain

class EditSupplierUseCase(private val repository: SupplierRepository) {

    suspend fun invoke(supplier: SuppliersModel): SuppliersModel {
        return try {
            val rowsUpdateSupplier = repository.editSupplier(supplier = supplier.toDatabase())
            return if (rowsUpdateSupplier > 0){
                repository.getSupplierByID(supplier.id)?.toDomain()
                    ?: throw Exception("Failed to retrieve updated supplier from database")
            } else {
                throw Exception("Failed to update supplier into database")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("EditSupplierUseCase", "Error update supplier", e)
            throw e // o puedes manejarlo de otra forma
        }
    }

}