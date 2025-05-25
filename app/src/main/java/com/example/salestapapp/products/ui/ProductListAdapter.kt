package com.example.salestapapp.products.ui

import android.app.AlertDialog
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
import com.example.salestapapp.products.data.model.ProductModel

class ProductListAdapter(private var list: List<ProductModel>,
val onItemRemove:(ProductModel) -> Unit, val onItemGoEdit: (ProductModel) ->
Unit):RecyclerView.Adapter<ProductsVH>() {

    private lateinit var context: Context
    //https://www.youtube.com/watch?v=8_3m2Ijp76o

    fun updateList (newList: List<ProductModel>){
        val productDiff = ProductsDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(productDiff)
        list = newList
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsVH {
        context = parent.context
        return ProductsVH(LayoutInflater.from(parent.context).inflate(R.layout.product_list_cardview, parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ProductsVH, position: Int) {
        holder.render(list[position], context, onItemRemove, onItemGoEdit)
    }


}

class ProductsVH(view: View):RecyclerView.ViewHolder(view){

    private val ivImage = view.findViewById<ImageView>(R.id.ivImageSupplierCardview)
    private val tvName = view.findViewById<TextView>(R.id.tvNameProductCardiView)
    private val tvCategory = view.findViewById<TextView>(R.id.tvCategoryProductCardiView)
    private val tvSupplier = view.findViewById<TextView>(R.id.tvSupplierProductCardiView)
    private val tvStock = view.findViewById<TextView>(R.id.tvStockProductCardiView)
    private val tvPrice = view.findViewById<TextView>(R.id.tvPriceProductCardiView)
    private val btnEdit = view.findViewById<Button>(R.id.btnEditProdCardView)
    private val btnDelete = view.findViewById<Button>(R.id.btnDeleteProductCardView)

    fun render(
        productModel: ProductModel,
        context: Context,
        onItemRemove: (ProductModel) -> Unit,
        onItemGoEdit: (ProductModel) -> Unit
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
        btnEdit.setOnClickListener {
           /* val intent = Intent(context, ProductsContainerActivity::class.java)
            intent.putExtra("idProduct", productModel.id)
            context.startActivity(intent)*/
            onItemGoEdit(productModel)
        }
        btnDelete.setOnClickListener {
            //modificar el entity de prodcutos para agrear la columna de enviado y la columna de Eliminado
            //llamar un Dialog para preguntar si se va elimar el producto
            //dialogDelete(context,productModel.name, productModel.id)
            onItemRemove(productModel)// mando el evento clic a fragmento o activity
        }
    }

    private fun convertBase64ToBitmap(base64String: String): Bitmap? {
        val decodedByteArray: ByteArray = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
    }

}