package com.example.salestapapp.reports.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestapapp.reports.data.domain.usecase.GetCashCutReportUseCase
import com.example.salestapapp.reports.data.model.CashCutModel
import com.example.salestapapp.sales.data.model.SalesModel
import kotlinx.coroutines.launch

class CashCutReportViewModel(
    private val getCashCut: GetCashCutReportUseCase
): ViewModel() {

    private val _getCashCut = MutableLiveData<CashCutModel>()
    val getCashCutL: LiveData<CashCutModel> = _getCashCut
    private val _getSales = MutableLiveData<List<SalesModel>>()
    val getSales: LiveData<List<SalesModel>> = _getSales

    fun invoke(date: String, startTime: String, endTime: String) {
        viewModelScope.launch {
            val result = getCashCut.invoke(date,startTime,endTime)
            _getCashCut.postValue(result.first)
            _getSales.postValue(result.second)
        }
    }

}