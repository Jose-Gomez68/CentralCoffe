package com.example.salestapapp.sales.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestapapp.products.data.domain.GetProductsUseCase
import com.example.salestapapp.products.data.model.ProductModel
import com.example.salestapapp.sales.data.domain.GetSalesWithDetailsUseCase
import com.example.salestapapp.sales.data.domain.InsertSalesUseCase
import com.example.salestapapp.sales.data.model.SaleWithDetailsModel
import com.example.salestapapp.sales.data.model.SalesDetailsModel
import com.example.salestapapp.sales.data.model.SalesModel
import kotlinx.coroutines.launch

class NewSalesViewModel(
    private val insertUseCase: InsertSalesUseCase,
    private val getSalesWithDetail: GetSalesWithDetailsUseCase,
    private val getAllProductsUseCase: GetProductsUseCase
): ViewModel() {

    private val _insertResult = MutableLiveData<Int?>()
    val insertResult: LiveData<Int?> = _insertResult

    private val _productModel = MutableLiveData<List<ProductModel>>()
    val productModel: LiveData<List<ProductModel>> = _productModel

    private val _saleModel = MutableLiveData<SaleWithDetailsModel?>()
    val saleModel: LiveData<SaleWithDetailsModel?> = _saleModel

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

    fun fetchSaleWithDetails(saleId: Int) {
        viewModelScope.launch {
            try {
                val sale = getSalesWithDetail(saleId) // crear use case
                _saleModel.postValue(sale)
            } catch (e: Exception) {
                e.printStackTrace()
                _saleModel.postValue(null)
            }
        }
    }

}