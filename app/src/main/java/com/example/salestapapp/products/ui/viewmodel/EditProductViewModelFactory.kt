package com.example.salestapapp.products.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.products.data.domain.EditProductUseCase

class EditProductViewModelFactory(private val editProductUseCase: EditProductUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return EditProductViewModel(editProductUseCase) as T
    }

}