package com.example.salestapapp.category.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.category.data.domain.DeleteCategoryByIdUseCase
import com.example.salestapapp.category.data.domain.GetCategoryUseCase

class CategoryListViewModelFactory(
    private val getCategory: GetCategoryUseCase,
    private val deleteCategory: DeleteCategoryByIdUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryListViewModel(getCategory, deleteCategory) as T
    }

}