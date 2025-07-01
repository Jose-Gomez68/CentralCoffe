package com.example.salestapapp.user.ui.view

import androidx.recyclerview.widget.DiffUtil
import com.example.salestapapp.login.data.model.UsersModel

class UserDiffUtil(
    private val oldList: List<UsersModel>,
    private val newList: List<UsersModel>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id // si el elemento que selecciono es el mismo
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }


}