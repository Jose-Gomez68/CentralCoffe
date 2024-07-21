package com.example.salestapapp.supplier.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.salestapapp.R
import com.example.salestapapp.supplier.data.model.SuppliersModel

class SupplierListAdapter(private var list: List<SuppliersModel>,
    val onItemRemove:(SuppliersModel) -> Unit, val onItemGoEdit: (SuppliersModel) -> Unit
):RecyclerView.Adapter<SupplierVH>() {

    private lateinit var context: Context

    fun updateList (newList: List<SuppliersModel>){
        val supplierDiff = SupplierDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(supplierDiff)
        list = newList
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplierVH {
        context = parent.context//todo CAMBIA EL XML DEL DISEŃO
        return SupplierVH(LayoutInflater.from(parent.context).inflate(R.layout.product_list_cardview, parent, false))
    }

    override fun onBindViewHolder(holder: SupplierVH, position: Int) {
        holder.render(list[position], context, onItemRemove, onItemGoEdit)
    }

    override fun getItemCount(): Int = list.size
}

class SupplierVH(view: View):RecyclerView.ViewHolder(view){

    private val ivImage = view.findViewById<ImageView>(R.id.ivImageSupplierCardview)
    private val tvName = view.findViewById<TextView>(R.id.tvNameProductCardiView)
    private val tvCategory = view.findViewById<TextView>(R.id.tvCategoryProductCardiView)
    private val tvSupplier = view.findViewById<TextView>(R.id.tvSupplierProductCardiView)
    private val tvStock1 = view.findViewById<TextView>(R.id.tvStockViewProductCardiView)
    private val tvStock = view.findViewById<TextView>(R.id.tvStockProductCardiView)
    private val tvPrice1 = view.findViewById<TextView>(R.id.tvPriceViewProductCardiView)
    private val tvPrice = view.findViewById<TextView>(R.id.tvPriceProductCardiView)
    private val btnEdit = view.findViewById<Button>(R.id.btnEditProdCardView)
    private val btnDelete = view.findViewById<Button>(R.id.btnDeleteProductCardView)

    fun render(
        supplierModel: SuppliersModel, context: Context,
        onItemRemove: (SuppliersModel) -> Unit,
        onItemGoEdit: (SuppliersModel) -> Unit
    ){
        if (supplierModel.imageSupplier.isNotEmpty()) {
            ivImage.setImageBitmap(convertBase64ToBitmap(supplierModel.imageSupplier))
        }else{
            ivImage.setImageResource(R.drawable.gallery)
        }
        tvName.text = supplierModel.name
        tvCategory.text = supplierModel.address
        tvSupplier.text = supplierModel.phone
        tvStock1.text = "Fecha Alta"
        tvStock.text = supplierModel.createDate
        tvPrice1.visibility = View.GONE
        tvPrice.visibility = View.GONE
        btnEdit.setOnClickListener {
            onItemGoEdit(supplierModel)
        }
        btnDelete.setOnClickListener {
            onItemRemove(supplierModel)// mando el evento clic a fragmento o activity
        }
    }

    private fun convertBase64ToBitmap(base64String: String): Bitmap? {
        val decodedByteArray: ByteArray = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
    }

}