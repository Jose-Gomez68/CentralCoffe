package com.example.salestapapp.sales.data.domain

import com.example.salestapapp.sales.data.SalesRepository
import com.example.salestapapp.sales.data.model.SaleWithDetailsModel
import com.example.salestapapp.sales.data.model.SalesDetailsModel
import com.example.salestapapp.sales.data.model.toDomain

class GetSalesWithDetailsUseCase(private val repository: SalesRepository) {

    suspend operator fun invoke(saleID: Int): SaleWithDetailsModel? {
        val saleByID = repository.getSaleById(saleID)
        val saleDetailByID = repository.getSalesDetailsListById(saleID)//revisar por que la query no trae el listado cnn el mismo id venta

        val details = saleDetailByID.map {
            SalesDetailsModel(
            it.id,
            it.saleId,
            it.productId,
            it.productName,
            it.quantity,
            it.unitPrice,
            it.totalPrice

        ) }

        val saleWithDetail = SaleWithDetailsModel(
            saleByID.toDomain(),
            details
        )

        return if (saleByID != null && saleDetailByID.isNotEmpty()){
            saleWithDetail
        } else {
            null
        }

    }

}