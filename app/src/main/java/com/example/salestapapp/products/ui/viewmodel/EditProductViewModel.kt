package com.example.salestapapp.products.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestapapp.category.data.domain.GetCategoryUseCase
import com.example.salestapapp.category.data.model.CategoryModel
import com.example.salestapapp.products.data.domain.EditProductUseCase
import com.example.salestapapp.products.data.domain.GetProductByIdUseCase
import com.example.salestapapp.products.data.model.ProductModel
import com.example.salestapapp.supplier.data.domain.usecase.GetSuppliersUseCase
import com.example.salestapapp.supplier.data.model.SuppliersModel
import kotlinx.coroutines.launch

class EditProductViewModel(
    private val editProduct: EditProductUseCase,
    private val getProducts: GetProductByIdUseCase,
    private val getCategory: GetCategoryUseCase,
    private val getSupplier: GetSuppliersUseCase
) : ViewModel() {

    private val _editProdModel = MutableLiveData<ProductModel>()
    val editProdModel: LiveData<ProductModel> = _editProdModel

    private val _productModel = MutableLiveData<ProductModel>()
    val productModel: LiveData<ProductModel> = _productModel

    private val _categoryModel = MutableLiveData<List<CategoryModel>>()
    val categoryModel: LiveData<List<CategoryModel>> = _categoryModel

    private val _supplierModel = MutableLiveData<List<SuppliersModel>>()

    private val _combinedData = MediatorLiveData<Triple<ProductModel, List<CategoryModel>, List<SuppliersModel>>>()
    val combinedData: LiveData<Triple<ProductModel, List<CategoryModel>, List<SuppliersModel>>> = _combinedData


    // Almacenan los valores actuales para combinarlos
    private var latestProduct: ProductModel? = null
    private var latestCategories: List<CategoryModel>? = null
    private var latestSupplier: List<SuppliersModel>? = null

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

    init {
        _combinedData.addSource(_productModel) { product ->
            latestProduct = product
            emitIfBothAvailable()
        }

        _combinedData.addSource(_categoryModel) { categories ->
            latestCategories = categories
            emitIfBothAvailable()
        }

        _combinedData.addSource(_supplierModel) { supplier ->
            latestSupplier = supplier
            emitIfBothAvailable()
        }
    }

    private fun emitIfBothAvailable() {
        val product = latestProduct
        val categories = latestCategories
        val suppliers = latestSupplier

        if (product != null && categories != null && suppliers != null) {
            _combinedData.value = Triple(product, categories, suppliers)
        }
    }

    fun loadData(productID: Int) {
        // Obtiene producto y categorías en paralelo
        getProduct(productID)
        getCategory()
        getSupplier()
    }

}