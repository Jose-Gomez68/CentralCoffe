package com.example.salestapapp.supplier.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.supplier.data.domain.usecase.SaveSupplierUseCase

class NewSupplierViewModelFactory(private val saveSupplierUseCase: SaveSupplierUseCase) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewSupplierViewModel(saveSupplierUseCase) as T
    }

}