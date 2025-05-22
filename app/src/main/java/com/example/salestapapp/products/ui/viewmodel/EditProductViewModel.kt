package com.example.salestapapp.products.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestapapp.products.data.domain.EditProductUseCase
import com.example.salestapapp.products.data.domain.GetProductByIdUseCase
import com.example.salestapapp.products.data.domain.GetProductsUseCase
import com.example.salestapapp.products.data.model.ProductModel
import kotlinx.coroutines.launch

class EditProductViewModel(
    private val editProduct: EditProductUseCase,
    private val getProducts: GetProductByIdUseCase
) : ViewModel() {

    private val _editProdModel = MutableLiveData<ProductModel>()
    val editProdModel: LiveData<ProductModel> = _editProdModel

    private val _productModel = MutableLiveData<ProductModel>()
    val productModel: LiveData<ProductModel> = _productModel

    fun onCreate (productModel: ProductModel) {
        viewModelScope.launch{
            //logica
            val result = editProduct.invoke(productModel)
            _editProdModel.postValue(result)

        }
    }

    fun getProduct(productID: Int) {
        viewModelScope.launch {
            val result = getProducts.invoke(productID)
            _productModel.postValue(result)
        }
    }

}