package com.example.salestapapp.reports.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.salestapapp.reports.data.model.ProductosMasVendidosModel

class ProductReportMasDIffUtil(
    private val oldList: List<ProductosMasVendidosModel>,
    private val newList: List<ProductosMasVendidosModel>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].productId == newList[newItemPosition].productId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}