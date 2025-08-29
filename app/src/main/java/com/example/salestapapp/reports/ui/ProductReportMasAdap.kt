package com.example.salestapapp.reports.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.salestapapp.R
import com.example.salestapapp.reports.data.model.ProductosMasVendidosModel

class ProductReportMasAdap(
    private var list: List<ProductosMasVendidosModel>
): RecyclerView.Adapter<ProductosMasVendidosVH>() {

    private lateinit var context: Context

    fun updateListMas(newList: List<ProductosMasVendidosModel>) {
        val productMasRDiff = ProductReportMasDIffUtil(list, newList)
        val result = DiffUtil.calculateDiff(productMasRDiff)
        list = newList
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductosMasVendidosVH {
        context = parent.context
        return ProductosMasVendidosVH(LayoutInflater.from(parent.context).inflate(R.layout.list_product_report_mas_vendido_card_view, parent, false))
    }

    override fun onBindViewHolder(holder: ProductosMasVendidosVH, position: Int) {
        holder.render(list[position])
    }

    override fun getItemCount(): Int = list.size


}

class ProductosMasVendidosVH(view: View): RecyclerView.ViewHolder(view) {

    private val codProd = view.findViewById<TextView>(R.id.tvCodProductProductReport)
    private val descrip = view.findViewById<TextView>(R.id.tvDescripMasVendProductReport)
    private val category = view.findViewById<TextView>(R.id.tvCategoryyProductReport)
    private val totalSales = view.findViewById<TextView>(R.id.tvTotalSalesProductReport)
    private val totalIngresos = view.findViewById<TextView>(R.id.tvIngresoGeneralProductReport)

    fun render(product: ProductosMasVendidosModel) {
        codProd.text = product.productId.toString()
        descrip.text = product.productName
        category.text = product.category
        totalSales.text = "${product.totalVendidos}" // total de unidades vendidas
        totalIngresos.text = "$ ${product.totalIngresos}" //total de precio que se genero por esas ventas
    }

}