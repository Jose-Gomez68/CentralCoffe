package com.example.salestapapp.supplier.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.supplier.data.domain.usecase.EditSupplierUseCase
import com.example.salestapapp.supplier.data.domain.usecase.GetSupplierByIdUseCase

class EditSupplierViewModelFactory(
    private val editSupplierUseCase: EditSupplierUseCase,
    private val getSupplierByIdUseCase: GetSupplierByIdUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  EditSupplierViewModel(
            editSupplierUseCase,
            getSupplierByIdUseCase
        ) as T
    }

}