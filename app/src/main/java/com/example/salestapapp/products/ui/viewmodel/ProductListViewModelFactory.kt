package com.example.salestapapp.products.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.products.data.domain.DeleteProductByIdUseCase
import com.example.salestapapp.products.data.domain.GetProductsUseCase

class ProductListViewModelFactory(private val getProduct: GetProductsUseCase,
                                  private val deleteProduct: DeleteProductByIdUseCase) : ViewModelProvider.Factory  {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductsListViewModel(getProduct, deleteProduct) as T
    }

}