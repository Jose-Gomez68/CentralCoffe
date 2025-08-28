package com.example.salestapapp.reports.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.reports.data.domain.usecase.GetProductReportInventUseCase

class GetInventReportViewModelFactory(
    private val getProductReportInventUseCase: GetProductReportInventUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GetInventReportViewModel(getProductReportInventUseCase) as T
    }

}