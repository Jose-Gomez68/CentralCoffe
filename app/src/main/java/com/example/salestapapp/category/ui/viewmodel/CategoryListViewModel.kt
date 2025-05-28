package com.example.salestapapp.category.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestapapp.category.data.domain.DeleteCategoryByIdUseCase
import com.example.salestapapp.category.data.domain.GetCategoryUseCase
import com.example.salestapapp.category.data.model.CategoryModel
import kotlinx.coroutines.launch

class CategoryListViewModel (
private val getCategory: GetCategoryUseCase,
private val deleteCategory: DeleteCategoryByIdUseCase
): ViewModel(){

    private val _categoryModel = MutableLiveData<List<CategoryModel>>()
    val categoryModel: LiveData<List<CategoryModel>> = _categoryModel

    fun onCreate() {
        viewModelScope.launch {
            val result = getCategory.invoke()
            if (!result.isNullOrEmpty()){
                _categoryModel.postValue(result)
            }
        }
    }

    fun removeCategory(category: CategoryModel) {
        viewModelScope.launch {
            deleteCategory.invoke(category)
            val currentList = _categoryModel.value.orEmpty().toMutableList()
            currentList.remove(category)
            _categoryModel.value = currentList
            Log.e("ELIMINANDO", "" + currentList.size)
        }
    }

}