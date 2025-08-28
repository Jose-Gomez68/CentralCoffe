package com.example.salestapapp.reports.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.salestapapp.products.data.model.ProductModel

class ProductReportDiffUtil(
    private val oldList: List<ProductModel>,
    private val newList: List<ProductModel>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}