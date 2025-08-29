package com.example.salestapapp.reports.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.reports.data.domain.usecase.GetMasVendidosUseCase
import com.example.salestapapp.reports.data.domain.usecase.GetProductReportInventUseCase

class GetInventReportViewModelFactory(
    private val getProductReportInventUseCase: GetProductReportInventUseCase,
    private val getMasVendidos: GetMasVendidosUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GetInventReportViewModel(getProductReportInventUseCase, getMasVendidos) as T
    }

}