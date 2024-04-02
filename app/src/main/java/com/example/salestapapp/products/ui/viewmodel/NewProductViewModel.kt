package com.example.salestapapp.products.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestapapp.products.data.domain.InsertProductUseCase
import com.example.salestapapp.products.data.model.ProductModel
import kotlinx.coroutines.launch

class NewProductViewModel(private val inserUseCase: InsertProductUseCase) : ViewModel()  {

    private val _newProdModel = MutableLiveData<ProductModel>()
    val newProdModel: LiveData<ProductModel> = _newProdModel

    fun onCreate (productModel: ProductModel) {
        viewModelScope.launch{
            //logica
            // Lógica de tu función
            val result = inserUseCase.invoke(productModel)
            _newProdModel.postValue(result)

        }
    }

}