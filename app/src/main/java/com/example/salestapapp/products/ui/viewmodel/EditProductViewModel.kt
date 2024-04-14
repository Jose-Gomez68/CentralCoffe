package com.example.salestapapp.products.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestapapp.products.data.domain.EditProductUseCase
import com.example.salestapapp.products.data.model.ProductModel
import kotlinx.coroutines.launch

class EditProductViewModel(private val editProduct: EditProductUseCase) : ViewModel() {

    private val _editProdModel = MutableLiveData<ProductModel>()
    val editProdModel: LiveData<ProductModel> = _editProdModel

    fun onCreate (productModel: ProductModel) {
        viewModelScope.launch{
            //logica
            // Lógica de tu función
            val result = editProduct.invoke(productModel)
            _editProdModel.postValue(result)

        }
    }

}