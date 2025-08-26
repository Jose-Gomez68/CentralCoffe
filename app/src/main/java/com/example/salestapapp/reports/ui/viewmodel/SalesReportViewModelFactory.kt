package com.example.salestapapp.reports.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.reports.data.domain.usecase.GetSalesReportUseCase

class SalesReportViewModelFactory(
    private val salesReport: GetSalesReportUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SalesReportViewModel(salesReport) as T
    }

}