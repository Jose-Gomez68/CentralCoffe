package com.example.salestapapp.category.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.salestapapp.R
import com.example.salestapapp.category.data.model.CategoryModel

class CategoryListAdapter(private var list: List<CategoryModel>,
    val onItemRemove:(CategoryModel) -> Unit, val onItemGoEdit: (CategoryModel) -> Unit
): RecyclerView.Adapter<CategoryVH>() {

    private lateinit var context: Context
    //https://www.youtube.com/watch?v=8_3m2Ijp76o

    fun updateList(newList: List<CategoryModel>){
        val categoryDiff = CategoryDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(categoryDiff)
        list = newList
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        context = parent.context
        return CategoryVH(LayoutInflater.from(parent.context).inflate(R.layout.category_list_cardview, parent, false))
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        holder.render(list[position], context, onItemRemove, onItemGoEdit)
    }

    override fun getItemCount(): Int = list.size
}

class CategoryVH(view: View): RecyclerView.ViewHolder(view){

    private val tvName = view.findViewById<TextView>(R.id.tvNameCategoryCardiView)
    private val btnEdit = view.findViewById<Button>(R.id.btnEditCategoryCardView)
    private val btnDelete = view.findViewById<Button>(R.id.btnDeleteCategoryCardView)

    fun render(
        categoryModel: CategoryModel,
        context: Context,
        onItemRemove: (CategoryModel) -> Unit,
        onItemGoEdit: (CategoryModel) -> Unit
    ){

        tvName.text = categoryModel.name
        btnEdit.setOnClickListener {
            onItemGoEdit(categoryModel)
        }
        btnDelete.setOnClickListener {
            onItemRemove(categoryModel)
        }

    }

}