package com.example.salestapapp.supplier.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestapapp.supplier.data.domain.SaveSupplierUseCase
import com.example.salestapapp.supplier.data.model.SuppliersModel
import kotlinx.coroutines.launch

class NewSupplierViewModel(private val saveSupplierUseCase: SaveSupplierUseCase): ViewModel() {

    private val _newSupplierModel = MutableLiveData<SuppliersModel>()
    val newSupplierModel: LiveData<SuppliersModel> = _newSupplierModel

    fun onCreate (supplierModel: SuppliersModel) {
        viewModelScope.launch {
            val result = saveSupplierUseCase.invoke(supplierModel)
            _newSupplierModel.postValue(result)
        }
    }
}