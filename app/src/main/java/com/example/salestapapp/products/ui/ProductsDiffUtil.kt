package com.example.salestapapp.products.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.salestapapp.products.data.model.ProductModel

class ProductsDiffUtil(
    private val oldList:List<ProductModel>,
    private val newList: List<ProductModel>
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