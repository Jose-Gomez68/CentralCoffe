package com.example.salestapapp.category.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestapapp.category.data.domain.EditCategoryUseCase
import com.example.salestapapp.category.data.domain.GetCategoryByIdUseCase
import com.example.salestapapp.category.data.model.CategoryModel
import kotlinx.coroutines.launch

class EditCategoryViewModel(
    private val editCategory: EditCategoryUseCase,
    private val getCategory: GetCategoryByIdUseCase
): ViewModel() {

    private val _editCategoryModel = MutableLiveData<CategoryModel>()
    val editCategoryModel: LiveData<CategoryModel> = _editCategoryModel

    private val _categoryModel = MutableLiveData<CategoryModel>()
    val categoryModel: LiveData<CategoryModel> = _categoryModel

    fun onCreate (categoryModel: CategoryModel) {
        viewModelScope.launch{
            //logica
            val result = editCategory.invoke(categoryModel)
            _editCategoryModel.postValue(result)

        }
    }

    fun getCategory(categoryID: Int) {
        viewModelScope.launch {
            val result = getCategory.invoke(categoryID)
            _categoryModel.postValue(result)
        }
    }

}