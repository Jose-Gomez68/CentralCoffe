package com.example.salestapapp.products.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestapapp.products.data.domain.GetProductsUseCase
import com.example.salestapapp.products.data.domain.InsertProductUseCase
import com.example.salestapapp.products.data.model.ProductModel
import kotlinx.coroutines.launch

class ProductsListViewModel(private val getProduct: GetProductsUseCase): ViewModel() {

    val productModel = MutableLiveData<List<ProductModel>>()
    fun onCreate() {
        viewModelScope.launch {
            val result = getProduct.invoke()

            if (!result.isNullOrEmpty()){
                productModel.postValue(result)
            }
        }
    }

}