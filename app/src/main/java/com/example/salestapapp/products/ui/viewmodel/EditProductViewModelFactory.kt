package com.example.salestapapp.products.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.products.data.domain.EditProductUseCase
import com.example.salestapapp.products.data.domain.GetProductByIdUseCase

class EditProductViewModelFactory(private val editProductUseCase: EditProductUseCase,
private val getProductByIdUseCase: GetProductByIdUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return EditProductViewModel(
            editProductUseCase,
            getProductByIdUseCase
        ) as T
    }

}