package com.example.salestapapp.supplier.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.supplier.data.domain.usecase.DeleteSupplierByIDUseCase
import com.example.salestapapp.supplier.data.domain.usecase.GetSuppliersUseCase

class SupplierViewModelFactory(
    private val getSupplier: GetSuppliersUseCase,
    private val deleteSupplier: DeleteSupplierByIDUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SupplierViewModel(getSupplier, deleteSupplier) as T
    }

}