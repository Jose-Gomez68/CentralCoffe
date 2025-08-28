package com.example.salestapapp.reports.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.salestapapp.R
import com.example.salestapapp.products.data.model.ProductModel
import com.example.salestapapp.sales.data.model.SalesModel

class InventProductReportAdapter(
    private var list: List<ProductModel>
): RecyclerView.Adapter<InventProductsReportVH>() {

    private lateinit var context: Context

    fun updateLis(newList: List<ProductModel>) {
        val productRDiff = ProductReportDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(productRDiff)
        list = newList
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventProductsReportVH {
        context = parent.context
        return InventProductsReportVH(LayoutInflater.from(parent.context).inflate(R.layout.list_product_report_invent_card_view, parent, false))
    }

    override fun onBindViewHolder(holder: InventProductsReportVH, position: Int) {
        holder.render(list[position])
    }

    override fun getItemCount(): Int = list.size

}

class InventProductsReportVH(view: View): RecyclerView.ViewHolder(view) {

    private val codProd = view.findViewById<TextView>(R.id.tvCodProductProductReport)
    private val prod = view.findViewById<TextView>(R.id.tvProductProductReport)
    private val category = view.findViewById<TextView>(R.id.tvCategoryProductReport)
    private val stock = view.findViewById<TextView>(R.id.tvStcokProductReport)
    private val unitPrice = view.findViewById<TextView>(R.id.tvUnitPriceProductReport)
    private val valorInvent = view.findViewById<TextView>(R.id.tvValorInventProductReport)
    private val updateDate = view.findViewById<TextView>(R.id.tvUpdateDateProductReport)

    fun render(productModel: ProductModel) {

        var valor = productModel.quantity * productModel.price

        codProd.text = productModel.id.toString()
        prod.text = productModel.name
        category.text = productModel.category
        stock.text = productModel.quantity.toString()
        unitPrice.text = "$ ${productModel.price}"
        valorInvent.text = "$ $valor"
        updateDate.text = productModel.updateDate



    }

}