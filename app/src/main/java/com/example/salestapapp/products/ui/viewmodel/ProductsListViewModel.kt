package com.example.salestapapp.products.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestapapp.products.data.domain.DeleteProductByIdUseCase
import com.example.salestapapp.products.data.domain.GetProductsUseCase
import com.example.salestapapp.products.data.domain.InsertProductUseCase
import com.example.salestapapp.products.data.model.ProductModel
import kotlinx.coroutines.launch

class ProductsListViewModel(
    private val getProduct: GetProductsUseCase,
    private val deleteProduct: DeleteProductByIdUseCase
) : ViewModel() {

    private val _productModel = MutableLiveData<List<ProductModel>>()
    val productModel: LiveData<List<ProductModel>> = _productModel

    fun onCreate() {
        viewModelScope.launch {
            val result = getProduct.invoke()

            if (!result.isNullOrEmpty()){
                _productModel.postValue(result)
            }
        }
    }

    fun removeProduct(product: ProductModel) {
        viewModelScope.launch {
            deleteProduct.invoke(product)
            val currentList = _productModel.value.orEmpty().toMutableList()
            currentList.remove(product)
            _productModel.value = currentList
            Log.e("ELIMINANDO", "" + currentList.size)
        }
    }

}