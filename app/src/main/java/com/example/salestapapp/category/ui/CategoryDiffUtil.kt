package com.example.salestapapp.category.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.salestapapp.category.data.model.CategoryModel

class CategoryDiffUtil(
    private val oldList: List<CategoryModel>,
    private val newList: List<CategoryModel>
):DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id //si el elemento que selecciono es el mismo
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}