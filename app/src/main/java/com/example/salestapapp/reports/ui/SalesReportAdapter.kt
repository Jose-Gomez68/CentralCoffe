package com.example.salestapapp.reports.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.salestapapp.R
import com.example.salestapapp.sales.data.model.SalesModel

class SalesReportAdapter(
    private var list: List<SalesModel>
): RecyclerView.Adapter<SalesReportVH>() {

    private lateinit var context: Context

    fun updateLis(newList: List<SalesModel>) {
        val salesRDiff = SalesReportDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(salesRDiff)
        list = newList
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesReportVH {
        context = parent.context
        return SalesReportVH(LayoutInflater.from(parent.context).inflate(R.layout.list_sales_report_card_view, parent, false))
    }

    override fun onBindViewHolder(holder: SalesReportVH, position: Int) {
        holder.render(list[position])
    }

    override fun getItemCount(): Int = list.size


}

class SalesReportVH(view: View): RecyclerView.ViewHolder(view) {

    private val noVenta = view.findViewById<TextView>(R.id.tvIdSalesReport)
    private val subTotal = view.findViewById<TextView>(R.id.tvSubTotalSalesReport)
    private val total = view.findViewById<TextView>(R.id.tvTotalSalesReport)
    private val status = view.findViewById<TextView>(R.id.tvStatusSalesReport)
    private val method = view.findViewById<TextView>(R.id.tvMethodSalesReport)
    private val createDate = view.findViewById<TextView>(R.id.tvCreateDateSalesReport)

    fun render(salesReport: SalesModel) {

        noVenta.text = salesReport.id.toString()
        subTotal.text = salesReport.subTotal.toString()
        total.text = salesReport.total.toString()
        status.text = salesReport.statusSales
        method.text = salesReport.paymentMethod
        createDate.text = salesReport.createDate

    }

}