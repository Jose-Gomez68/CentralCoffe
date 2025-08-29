package com.example.salestapapp.reports.data.domain.usecase

import android.util.Log
import com.example.salestapapp.reports.data.model.ProductosMasVendidosModel
import com.example.salestapapp.sales.data.SalesRepository

class GetMasVendidosUseCase(
    private val repo: SalesRepository
) {

    suspend fun invoke(startDate: String, endDate: String): List<ProductosMasVendidosModel>  {
        return try {
            repo.getProductosMasVendidos(startDate, endDate)
        }catch (e: Exception) {
            e.printStackTrace()
            Log.e("GetMasVendidosUseCase", "Error get Report mas Vendidos", e)
            throw e
        }
    }

}