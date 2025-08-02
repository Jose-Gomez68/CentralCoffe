package com.example.salestapapp.sales.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestapapp.products.data.domain.GetProductsUseCase
import com.example.salestapapp.products.data.model.ProductModel
import com.example.salestapapp.sales.data.domain.InsertSalesUseCase
import com.example.salestapapp.sales.data.model.SalesDetailsModel
import com.example.salestapapp.sales.data.model.SalesModel
import kotlinx.coroutines.launch

class NewSalesViewModel(
    private val insertUseCase: InsertSalesUseCase,
    private val getAllProductsUseCase: GetProductsUseCase
): ViewModel() {

    private val _insertResult = MutableLiveData<Boolean>()
    val insertResult: LiveData<Boolean> = _insertResult

    private val _productModel = MutableLiveData<List<ProductModel>>()
    val productModel: LiveData<List<ProductModel>> = _productModel

    fun onCreate(salesModel: SalesModel, salesDetailsModel: List<SalesDetailsModel>){
        viewModelScope.launch {
            val result = insertUseCase.invoke(salesModel, salesDetailsModel)
            _insertResult.postValue(result)
        }
    }

    fun getALlProducts() {
        viewModelScope.launch {
            val products = getAllProductsUseCase.invoke()
            Log.e("AQUI2", "AAAA"+products)
            _productModel.postValue(products)
        }
    }

}