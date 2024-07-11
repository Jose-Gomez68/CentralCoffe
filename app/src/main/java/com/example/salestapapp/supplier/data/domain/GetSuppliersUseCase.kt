package com.example.salestapapp.supplier.data.domain

import com.example.salestapapp.supplier.data.SupplierRepository
import com.example.salestapapp.supplier.data.model.SuppliersModel
import com.example.salestapapp.supplier.data.model.toDomain

class GetSuppliersUseCase(private val repository: SupplierRepository) {

    suspend operator fun invoke(): List<SuppliersModel> {
        val suppliers = repository.getAllSuppliers().map {
            it.toDomain()
        }

        return if (suppliers.isNotEmpty()){
            suppliers
        }else{
            emptyList<SuppliersModel>()
        }

    }

}