package com.example.salestapapp.reports.data.domain.usecase

import android.util.Log
import com.example.salestapapp.sales.data.SalesRepository
import com.example.salestapapp.sales.data.model.SalesModel
import com.example.salestapapp.sales.data.model.toDomain

class GetSalesReportUseCase(
    private val repository: SalesRepository
) {

    suspend fun invoke(startDate: String, endDate: String): List<SalesModel>  {
        return try {
            repository.getSalesReport(startDate,endDate).map { it.toDomain() }
        }catch (e: Exception) {
            e.printStackTrace()
            Log.e("GetSalesReportUseCase", "Error get SalesReport", e)
            throw e
        }
    }

}