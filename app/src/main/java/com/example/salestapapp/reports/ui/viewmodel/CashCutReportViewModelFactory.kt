package com.example.salestapapp.reports.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.reports.data.domain.usecase.GetCashCutReportUseCase

class CashCutReportViewModelFactory(
    private val getCashCut: GetCashCutReportUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CashCutReportViewModel(getCashCut) as T
    }

}