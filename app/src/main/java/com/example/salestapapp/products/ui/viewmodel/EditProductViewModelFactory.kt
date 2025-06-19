package com.example.salestapapp.products.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.category.data.domain.GetCategoryUseCase
import com.example.salestapapp.products.data.domain.EditProductUseCase
import com.example.salestapapp.products.data.domain.GetProductByIdUseCase
import com.example.salestapapp.supplier.data.domain.usecase.GetSuppliersUseCase

class EditProductViewModelFactory(private val editProductUseCase: EditProductUseCase,
private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getCategoryUseCase: GetCategoryUseCase,
    private val getSuppliersUseCase: GetSuppliersUseCase
    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return EditProductViewModel(
            editProductUseCase,
            getProductByIdUseCase,
            getCategoryUseCase,
            getSuppliersUseCase
        ) as T
    }

}