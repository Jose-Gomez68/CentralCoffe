package com.example.salestapapp.supplier.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestapapp.supplier.data.domain.usecase.EditSupplierUseCase
import com.example.salestapapp.supplier.data.domain.usecase.GetSupplierByIdUseCase
import com.example.salestapapp.supplier.data.model.SuppliersModel
import kotlinx.coroutines.launch

class EditSupplierViewModel(
    private val editSupplier: EditSupplierUseCase,
    private val getSupplier: GetSupplierByIdUseCase
): ViewModel() {

    private val _editSupplierModel = MutableLiveData<SuppliersModel>()
    val editSupplierModel: LiveData<SuppliersModel> = _editSupplierModel

    private val _supplierModel = MutableLiveData<SuppliersModel>()
    val supplierModel: LiveData<SuppliersModel> = _supplierModel

    fun onCreate (suppliersModel: SuppliersModel) {
        viewModelScope.launch {
            val result = editSupplier.invoke(suppliersModel)
            _editSupplierModel.postValue(result)
        }
    }

    fun getSupplier(supplierID: Int){
        viewModelScope.launch {
            val result = getSupplier.invoke(supplierID)
            _supplierModel.postValue(result)
        }
    }

}