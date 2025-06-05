package com.example.salestapapp.category.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.category.data.domain.EditCategoryUseCase
import com.example.salestapapp.category.data.domain.GetCategoryByIdUseCase

class EditCategoryViewModelFactory(
    private val editCategoryUseCase: EditCategoryUseCase,
    private val getCategoryByUseCase: GetCategoryByIdUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return EditCategoryViewModel(
            editCategoryUseCase,
            getCategoryByUseCase
        ) as T
    }

}