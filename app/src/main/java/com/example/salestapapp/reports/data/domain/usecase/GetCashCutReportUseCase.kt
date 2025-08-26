package com.example.salestapapp.reports.data.domain.usecase

import android.util.Log
import com.example.salestapapp.reports.data.model.CashCutModel
import com.example.salestapapp.sales.data.SalesRepository
import com.example.salestapapp.sales.data.model.SalesModel

class GetCashCutReportUseCase(
    private val repository: SalesRepository
) {

    suspend fun invoke(date: String, startTime: String, endTime: String): Pair<CashCutModel, List<SalesModel>>  {
        return try {
            repository.getCashCut(date,startTime,endTime)
        }catch (e: Exception) {
            e.printStackTrace()
            Log.e("EditSupplierUseCase", "Error update supplier", e)
            throw e
        }
    }

}