package com.example.salestapapp.products.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.category.data.domain.GetCategoryUseCase
import com.example.salestapapp.products.data.domain.InsertProductUseCase
import com.example.salestapapp.supplier.data.domain.usecase.GetSuppliersUseCase

class NewProductViewModelFactory(
    private val inserUseCase: InsertProductUseCase,
    private val getCategoryUseCase: GetCategoryUseCase,
    private val getSuppliersUseCase: GetSuppliersUseCase
) : ViewModelProvider.Factory  {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewProductViewModel(
            inserUseCase,
            getCategoryUseCase,
            getSuppliersUseCase
        ) as T
    }

}