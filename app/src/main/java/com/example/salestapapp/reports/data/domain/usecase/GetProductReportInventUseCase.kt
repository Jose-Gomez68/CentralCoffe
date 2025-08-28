package com.example.salestapapp.reports.data.domain.usecase

import android.util.Log
import com.example.salestapapp.products.data.ProductsRepository
import com.example.salestapapp.products.data.model.ProductModel
import com.example.salestapapp.products.data.model.toDomain

class GetProductReportInventUseCase(
    private val repo: ProductsRepository
) {

    suspend fun invoke(): List<ProductModel> {

        return try {
            repo.getAllData().map { it.toDomain() }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("GetProductReportInventUseCase", "Error get ProductReportInventa", e)
            throw e
        }

    }

}