package com.example.salestapapp.supplier.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestapapp.supplier.data.domain.usecase.DeleteSupplierByIDUseCase
import com.example.salestapapp.supplier.data.domain.usecase.GetSuppliersUseCase
import com.example.salestapapp.supplier.data.model.SuppliersModel
import kotlinx.coroutines.launch

class SupplierViewModel(
    private val getSuppliers: GetSuppliersUseCase,
    private val deleteSupplier: DeleteSupplierByIDUseCase
): ViewModel() {

    private val _supplierModel = MutableLiveData<List<SuppliersModel>>()
    val supplierModel: LiveData<List<SuppliersModel>> = _supplierModel

    fun onCreate() {
        viewModelScope.launch {
            val result = getSuppliers.invoke()

            if (!result.isNotEmpty()){
                _supplierModel.postValue(result)
            }
        }
    }

    fun removeSuppliers(supplier: SuppliersModel){
        viewModelScope.launch {
            deleteSupplier.invoke(supplier)
            val currentList = _supplierModel.value.orEmpty().toMutableList()
            currentList.remove(supplier)
            Log.e("Eliminando proovedor", "${currentList.size}")
        }
    }

}