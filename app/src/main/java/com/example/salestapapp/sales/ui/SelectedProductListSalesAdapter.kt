package com.example.salestapapp.sales.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.salestapapp.R
import com.example.salestapapp.products.data.model.ProductModel

class SelectedProductListSalesAdapter(
    private var list: List<ProductModel>,
    val onClickItem: (ProductModel) -> Unit
): RecyclerView.Adapter<SelectedProductsSalesVH>() {

    private lateinit var context: Context

    fun updateList (newList: List<ProductModel>){
        val productDiff = SelectProductListSalesDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(productDiff)
        list = newList
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedProductsSalesVH {
        context = parent.context
        return SelectedProductsSalesVH(LayoutInflater.from(parent.context).inflate(R.layout.dialog_product_list_sales_cardview, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: SelectedProductsSalesVH, position: Int) {
        holder.render(list[position], onClickItem)
    }

}

class SelectedProductsSalesVH(view: View): RecyclerView.ViewHolder(view) {

    private val ivImage = view.findViewById<ImageView>(R.id.ivImageProductCardviewDialogS)
    private val tvName = view.findViewById<TextView>(R.id.tvNameProductCardiViewDialogS)
    private val tvCategory = view.findViewById<TextView>(R.id.tvCategoryProductCardiViewDialogS)
    private val tvSupplier = view.findViewById<TextView>(R.id.tvSupplierProductCardiViewDialogS)
    private val tvStock = view.findViewById<TextView>(R.id.tvStockProductCardiViewDialogS)
    private val tvPrice = view.findViewById<TextView>(R.id.tvPriceProductCardiViewDialogS)
    private val card = view.findViewById<CardView>(R.id.cvContentProductListDialogS)

    fun render(
        productModel: ProductModel,
        onClickItem: (ProductModel) -> Unit
    ) {
        if (productModel.image.isNotEmpty()) {
            ivImage.setImageBitmap(convertBase64ToBitmap(productModel.image))
        }else{
            ivImage.setImageResource(R.drawable.gallery)
        }
        tvName.text = productModel.name
        tvCategory.text = productModel.category
        tvSupplier.text = productModel.supplier
        tvStock.text = productModel.quantity.toString()
        tvPrice.text = productModel.price.toString()

        card.setOnClickListener {
            onClickItem(productModel)
        }

    }

    private fun convertBase64ToBitmap(base64String: String): Bitmap? {
        val decodedByteArray: ByteArray = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
    }

}