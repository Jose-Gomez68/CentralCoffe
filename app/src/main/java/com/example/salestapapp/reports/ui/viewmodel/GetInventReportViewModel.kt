package com.example.salestapapp.reports.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestapapp.products.data.model.ProductModel
import com.example.salestapapp.reports.data.domain.usecase.GetMasVendidosUseCase
import com.example.salestapapp.reports.data.domain.usecase.GetProductReportInventUseCase
import com.example.salestapapp.reports.data.model.ProductosMasVendidosModel
import kotlinx.coroutines.launch

class GetInventReportViewModel(
    private val getProductReportInventUseCase: GetProductReportInventUseCase,
    private val getMasVendidos: GetMasVendidosUseCase
): ViewModel() {

    private val _getReportInvent = MutableLiveData<List<ProductModel>>()
    val getReportInvent: LiveData<List<ProductModel>> = _getReportInvent

    private val _getMas = MutableLiveData<List<ProductosMasVendidosModel>>()
    val getMas: LiveData<List<ProductosMasVendidosModel>> = _getMas

    fun invoke() {
        viewModelScope.launch {
            val result = getProductReportInventUseCase.invoke()
            _getReportInvent.postValue(result)
        }
    }

    fun masVendidos(startDate: String, endDate: String) {
        viewModelScope.launch {
            val result = getMasVendidos.invoke(startDate, endDate)
            _getMas.postValue(result)
        }
    }

}