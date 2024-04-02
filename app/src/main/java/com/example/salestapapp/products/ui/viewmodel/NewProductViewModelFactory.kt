package com.example.salestapapp.products.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.products.data.domain.InsertProductUseCase

class NewProductViewModelFactory(private val inserUseCase: InsertProductUseCase) : ViewModelProvider.Factory  {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewProductViewModel(inserUseCase) as T
    }

}