package com.example.salestapapp.sales.data.domain

import com.example.salestapapp.sales.data.SalesRepository
import com.example.salestapapp.sales.data.database.entities.toDatabase
import com.example.salestapapp.sales.data.model.SalesDetailsModel
import com.example.salestapapp.sales.data.model.SalesModel

class InsertSalesUseCase(
    private val repository: SalesRepository
) {

    suspend operator fun invoke(salesModel: SalesModel, salesDetailsModel: List<SalesDetailsModel>): Int? {

        val saleId = repository.insertSaleAndDetailsTransaction(
            salesModel.toDatabase(),
            salesDetailsModel.map { it.toDatabase() }
        )

        if (saleId != null && saleId > 0) {
            return saleId
        } else {
            throw Exception("Failed to insert sales into database")
        }

    }

}