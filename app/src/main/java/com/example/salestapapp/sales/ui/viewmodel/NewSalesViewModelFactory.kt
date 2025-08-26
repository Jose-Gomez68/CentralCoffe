package com.example.salestapapp.sales.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.products.data.domain.GetProductsUseCase
import com.example.salestapapp.sales.data.domain.GetSalesWithDetailsUseCase
import com.example.salestapapp.sales.data.domain.InsertSalesUseCase
import com.example.salestapapp.sales.data.domain.UpdateStockProductUseCase

class NewSalesViewModelFactory(
    private val insertUseCase: InsertSalesUseCase,
    private val getSalesWithDetail: GetSalesWithDetailsUseCase,
    private val getAllProductsUseCase: GetProductsUseCase,
    private val updateStockProductUseCase: UpdateStockProductUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewSalesViewModel(
            insertUseCase,
            getSalesWithDetail,
            getAllProductsUseCase,
            updateStockProductUseCase
        ) as T
    }

}