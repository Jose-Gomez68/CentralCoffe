package com.example.salestapapp.category.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestapapp.category.data.model.CategoryModel
import kotlinx.coroutines.launch

class NewCategortyViewModel(): ViewModel() {

    private val _newCategoryModel = MutableLiveData<CategoryModel>()
    val newCategoryModel: LiveData<CategoryModel> = _newCategoryModel

    fun onCreate (categoryModel: CategoryModel) {
        viewModelScope.launch {
            val result = insertUseCase.invoke(categoryModel)
        }
    }

}