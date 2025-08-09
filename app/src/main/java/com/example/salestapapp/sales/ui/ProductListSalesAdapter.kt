package com.example.salestapapp.sales.ui

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.salestapapp.R
import com.example.salestapapp.sales.data.model.SalesDetailsModel

class ProductListSalesAdapter(
    private var list: List<SalesDetailsModel>,
    val onMinusQuantity: (SalesDetailsModel) -> Unit,
    val onAddQueantity: (SalesDetailsModel) -> Unit,
    val onItemRemove: (SalesDetailsModel) -> Unit,
    val onQuantityChanged: (SalesDetailsModel, Int) -> Unit
):RecyclerView.Adapter<SalesDetailVH>() {

    private lateinit var context: Context

    fun updateList (newList: List<SalesDetailsModel>){
        val salesDetailDiff = SalesDetailDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(salesDetailDiff)
        list = newList
        result.dispatchUpdatesTo(this)


    }

    fun notifyAdapter() {
        notifyDataSetChanged()
    }

    fun getItemAt(position: Int): SalesDetailsModel = list[position]
    fun currentList(): List<SalesDetailsModel> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesDetailVH {
        context = parent.context
        return SalesDetailVH(LayoutInflater.from(context).inflate(R.layout.product_list_sale_cardview, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: SalesDetailVH, position: Int) {
        holder.render(list[position],onAddQueantity, onMinusQuantity, onItemRemove, onQuantityChanged)
    }


}

class SalesDetailVH(view: View): RecyclerView.ViewHolder(view){

    private val nameProduct = view.findViewById<TextView>(R.id.tvNameProductSalesList)
    private val quantity = view.findViewById<TextView>(R.id.tvQuantityProductSalesList)
    private val idProduct = view.findViewById<TextView>(R.id.tvIdProductSalesList)
    private val unitPrice = view.findViewById<TextView>(R.id.tvUnitPriceSalesList)
    private val btnAdd = view.findViewById<ImageButton>(R.id.btnAddProductSalesList)
    private val etQuantity = view.findViewById<EditText>(R.id.etQuantityProductSalesList)
    private val btnMinusProduct = view.findViewById<ImageButton>(R.id.btnMinusProductSalesList)
    private var currentTextWatcher: TextWatcher? = null

    fun render(
        salesDetailsModel: SalesDetailsModel,
        onAddQueantity: (SalesDetailsModel) -> Unit,
        onMinusQuantity: (SalesDetailsModel) -> Unit,
        onItemRemove: (SalesDetailsModel) -> Unit,
        onQuantityChanged: (SalesDetailsModel, Int) -> Unit
    ) {
        nameProduct.text = salesDetailsModel.productName
        quantity.text = "Cantidad: ${salesDetailsModel.quantity}"
        idProduct.text = "No Producto: ${salesDetailsModel.productId}"
        unitPrice.text = "Precio Unitario: $${salesDetailsModel.unitPrice}"
        currentTextWatcher?.let { etQuantity.removeTextChangedListener(it) }
        etQuantity.setText(salesDetailsModel.quantity.toString())
        etQuantity.setSelection(etQuantity.text.length)
        currentTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val newQty = s.toString().toIntOrNull() ?: 0
                if (newQty >= 0 &&newQty != salesDetailsModel.quantity) {
                    onQuantityChanged(salesDetailsModel, newQty)
                }
            }
        }
        etQuantity.addTextChangedListener(currentTextWatcher)
        btnAdd.setOnClickListener {
            onAddQueantity(salesDetailsModel)
        }
        btnMinusProduct.setOnClickListener {
            onMinusQuantity(salesDetailsModel)
        }

    }

    fun editQuantityEt(quantity: Int) : Int {
        etQuantity.setText(quantity.toString())
        return quantity
    }

}