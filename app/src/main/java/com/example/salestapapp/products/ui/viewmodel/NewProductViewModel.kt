package com.example.salestapapp.products.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestapapp.category.data.domain.GetCategoryUseCase
import com.example.salestapapp.category.data.model.CategoryModel
import com.example.salestapapp.products.data.domain.InsertProductUseCase
import com.example.salestapapp.products.data.model.ProductModel
import com.example.salestapapp.supplier.data.domain.usecase.GetSuppliersUseCase
import com.example.salestapapp.supplier.data.model.SuppliersModel
import kotlinx.coroutines.launch

class NewProductViewModel(
    private val inserUseCase: InsertProductUseCase,
    private val getCategory: GetCategoryUseCase,
    private val getSupplier: GetSuppliersUseCase
) : ViewModel()  {

    private val _newProdModel = MutableLiveData<ProductModel>()
    val newProdModel: LiveData<ProductModel> = _newProdModel

    private val _productModel = MutableLiveData<ProductModel>()
    val productModel: LiveData<ProductModel> = _productModel

    private val _categoryModel = MutableLiveData<List<CategoryModel>>()
    val categoryModel: LiveData<List<CategoryModel>> = _categoryModel

    private val _supplierModel = MutableLiveData<List<SuppliersModel>>()
    val supplierModel: LiveData<List<SuppliersModel>> = _supplierModel


    fun onCreate (productModel: ProductModel) {
        viewModelScope.launch{
            //logica
            // Lógica de tu función
            val result = inserUseCase.invoke(productModel)
            _newProdModel.postValue(result)

        }
    }

    fun getCategory() {
        viewModelScope.launch {
            val category = getCategory.invoke()
            _categoryModel.postValue(category)
        }
    }

    fun getSupplier() {
        viewModelScope.launch {
            val supplier = getSupplier.invoke()
            _supplierModel.postValue(supplier)
        }
    }

}