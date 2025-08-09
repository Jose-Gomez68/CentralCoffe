package com.example.salestapapp.sales.data.domain

import com.example.salestapapp.sales.data.SalesRepository
import com.example.salestapapp.sales.data.model.SaleWithDetailsModel
import com.example.salestapapp.sales.data.model.toDomain

class GetSalesWithDetailsUseCase(private val repository: SalesRepository) {

    suspend operator fun invoke(saleID: Int): SaleWithDetailsModel? {
        val saleByID = repository.getSaleById(saleID)
        val saleDetailByID = repository.getSalesDetailsListById(saleID)

        val saleWithDetail = SaleWithDetailsModel(
            saleByID.toDomain(),
            saleDetailByID.map { it.toDomain() }
        )

        return if (saleByID != null && saleDetailByID.isNotEmpty()){
            saleWithDetail
        } else {
            null
        }

    }

}