package com.example.salestapapp.supplier.data.domain.usecase

import android.database.sqlite.SQLiteException
import android.util.Log
import com.example.salestapapp.supplier.data.domain.repository.SupplierRepository
import com.example.salestapapp.supplier.data.model.SuppliersModel
import com.example.salestapapp.supplier.data.model.toDomain

class GetSupplierByIdUseCase(
    private val repository: SupplierRepository
) {

    suspend operator fun invoke(supplierID: Int): SuppliersModel {
        return try {
            val result = repository.getSupplierByID(supplierID)
            result.toDomain()
        }catch (e: SQLiteException) {
            Log.e("GetCategoryByIdUseCase", "Error de base de datos: ${e.message}", e)
            e.printStackTrace()
            SuppliersModel(
                0,
                "",
                "",
                "",
                "",
                ""
            )
        } catch (e: Exception){
            Log.e("GetSupplierByIdUseCase", "Error inesperado: ${e.message}", e)
            e.printStackTrace()
            SuppliersModel(
                0,
                "",
                "",
                "",
                "",
                ""
            )
        }
    }

}