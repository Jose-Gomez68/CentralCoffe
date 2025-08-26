package com.example.salestapapp.reports.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestapapp.reports.data.domain.usecase.GetSalesReportUseCase
import com.example.salestapapp.sales.data.model.SalesModel
import kotlinx.coroutines.launch

class SalesReportViewModel(
    private val getSalesReport: GetSalesReportUseCase
): ViewModel() {

    private val _getSalesReport = MutableLiveData<List<SalesModel>>()
    val getSalesReportt: LiveData<List<SalesModel>> = _getSalesReport

    fun invoke(startDate: String, endDate: String) {
        viewModelScope.launch {
            val result = getSalesReport.invoke(startDate,endDate)
            _getSalesReport.postValue(result)
        }
    }

}