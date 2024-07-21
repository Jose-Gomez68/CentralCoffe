package com.example.salestapapp.supplier.view

import androidx.recyclerview.widget.DiffUtil
import com.example.salestapapp.supplier.data.model.SuppliersModel

class SupplierDiffUtil(
    private val oldList:List<SuppliersModel>,
    private val newList:List<SuppliersModel>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id //si el elemento que selecciono es el mismo
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}