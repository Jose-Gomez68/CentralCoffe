package com.example.salestapapp.reports.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestapapp.products.data.model.ProductModel
import com.example.salestapapp.reports.data.domain.usecase.GetProductReportInventUseCase
import kotlinx.coroutines.launch

class GetInventReportViewModel(
    private val getProductReportInventUseCase: GetProductReportInventUseCase
): ViewModel() {

    private val _getReportInvent = MutableLiveData<List<ProductModel>>()
    val getReportInvent: LiveData<List<ProductModel>> = _getReportInvent

    fun invoke() {
        viewModelScope.launch {
            val result = getProductReportInventUseCase.invoke()
            _getReportInvent.postValue(result)
        }
    }

}