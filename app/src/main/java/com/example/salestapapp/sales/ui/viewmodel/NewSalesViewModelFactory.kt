package com.example.salestapapp.sales.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.products.data.domain.GetProductsUseCase
import com.example.salestapapp.sales.data.domain.InsertSalesUseCase

class NewSalesViewModelFactory(
    private val insertUseCase: InsertSalesUseCase,
    private val getAllProductsUseCase: GetProductsUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewSalesViewModel(
            insertUseCase,
            getAllProductsUseCase
        ) as T
    }

}