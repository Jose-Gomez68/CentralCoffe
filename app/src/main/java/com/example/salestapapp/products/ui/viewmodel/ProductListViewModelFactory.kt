package com.example.salestapapp.products.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.products.data.domain.GetProductsUseCase

class ProductListViewModelFactory(private val getProduct: GetProductsUseCase) : ViewModelProvider.Factory  {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductsListViewModel(getProduct) as T
    }

}