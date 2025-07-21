package com.example.salestapapp.sales.data.domain

import com.example.salestapapp.sales.data.SalesRepository
import com.example.salestapapp.sales.data.database.entities.toDatabase
import com.example.salestapapp.sales.data.model.SalesDetailsModel
import com.example.salestapapp.sales.data.model.SalesModel

class InsertSalesUseCase(
    private val repository: SalesRepository
) {

    suspend operator fun invoke(salesModel: SalesModel, salesDetailsModel: List<SalesDetailsModel>): Boolean {

        val insertSales = repository.insertSaleAndDetailsTransaction(
            salesModel.toDatabase(),
            salesDetailsModel.map { it.toDatabase() }
        )

        if (insertSales) {
            return true
        } else {
            throw Exception("Failed to insert sales into database")
        }

    }

}