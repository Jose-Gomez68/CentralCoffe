package com.example.salestapapp.category.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.category.data.domain.InsertCategoryUseCase

class NewCategoryViewModelFactory(
    private val insertUseCase: InsertCategoryUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewCategoryViewModel(insertUseCase) as T
    }

}