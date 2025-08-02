package com.example.salestapapp.sales.ui.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salestapapp.R
import com.example.salestapapp.databinding.FragmentSaleBinding
import com.example.salestapapp.products.data.ProductsRepository
import com.example.salestapapp.products.data.domain.GetProductsUseCase
import com.example.salestapapp.products.data.model.ProductModel
import com.example.salestapapp.rom.CyberCoffeAppDatabase
import com.example.salestapapp.rom.CyberCoffeDatabase
import com.example.salestapapp.sales.data.SalesRepository
import com.example.salestapapp.sales.data.domain.InsertSalesUseCase
import com.example.salestapapp.sales.data.model.SalesDetailsModel
import com.example.salestapapp.sales.ui.ProductListSalesAdapter
import com.example.salestapapp.sales.ui.viewmodel.NewSalesViewModel
import com.example.salestapapp.sales.ui.viewmodel.NewSalesViewModelFactory
import com.example.salestapapp.util.UtilsFunctions

class SaleFragment : Fragment() {

    private var _binding: FragmentSaleBinding? = null
    private val binding get() = _binding!!
    private lateinit var db:CyberCoffeDatabase
    private lateinit var util: UtilsFunctions
    private var productList: List<ProductModel> = emptyList()
    private var productListSalesDetail: MutableList<SalesDetailsModel> = mutableListOf()
    private lateinit var salesDetailAdap: ProductListSalesAdapter
    private lateinit var viewModel: NewSalesViewModel

            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = CyberCoffeAppDatabase.CyberCoffeAppDatabase.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSaleBinding.inflate(inflater, container, false)
        util = UtilsFunctions()
        val repository = SalesRepository(db)
        val repositoryProduct = ProductsRepository(db)
        val viewModelProviderFactory = NewSalesViewModelFactory(
            InsertSalesUseCase(repository),
            GetProductsUseCase(repositoryProduct)
        )

        viewModel = ViewModelProvider(
            this, viewModelProviderFactory
        )[NewSalesViewModel::class.java]

        viewModel.getALlProducts()

        binding.btnReturnSalesFrag.setOnClickListener {
            requireActivity().onBackPressed()
        }

        viewModel.productModel.observe(viewLifecycleOwner) { result ->

            productList = result

            /* salesDetailAdap = ProductListSalesAdapter(
                 sampleSalesDetails,
                 onMinusQuantity = {
                     Log.e("AQUI MINUS", "")
                 },
                 onAddQueantity = {
                     Log.e("AQUI ADD", "")
                 },
                 onItemRemove = {
                     Log.e("AQUI DELETE", "")
                 }
             )*/

        }

        binding.btnAddProductListSales.setOnClickListener {
            val dialog = SelectedProductListSales(
                context = requireContext(),
                productList = productList,
                onSelectedProd = { selectProd, quantity ->
                    Toast.makeText(requireContext(), "Aceptado", Toast.LENGTH_SHORT).show()

                    val existingProduct = productListSalesDetail.find { it.productId == selectProd.id }
                    if (existingProduct != null && verifyQuantityProductList(selectProd.id, existingProduct!!.quantity + quantity)){
                        existingProduct.quantity += quantity
                        existingProduct.totalPrice = existingProduct.quantity*existingProduct.unitPrice
                        Log.e("DENTRO DEL IF EXIST", existingProduct.toString())
                        salesDetailAdap.notifyAdapter()
                    } else if (existingProduct == null){
                        val newItem = SalesDetailsModel(
                            id = 0,
                            saleId = 0,
                            productId = selectProd.id,
                            productName = selectProd.name,
                            quantity = quantity,
                            unitPrice = selectProd.price,
                            totalPrice = quantity * selectProd.price
                        )

                        productListSalesDetail.add(newItem)
                        // Ahora actualiza el adaptador con la nueva lista
                        salesDetailAdap.updateList(productListSalesDetail.toList())
                        if (productListSalesDetail.count() == 1) {
                            salesDetailAdap.notifyAdapter()
                            Log.e("DENTRO DEL IF", productListSalesDetail.count().toString())
                        }

                        Log.e("SELECCIONADO", selectProd.toString())
                        Log.e("SELECCIONADO2", quantity.toString())
                        Log.e("SELECCIONADO3", productListSalesDetail.toString())
                    }

                    totalProducts(productListSalesDetail)

                },
                onCancel = {
                    Toast.makeText(requireContext(), "Cancelado", Toast.LENGTH_SHORT).show()
                }
            )
            dialog.show()
        }



        salesDetailAdap = ProductListSalesAdapter(
            productListSalesDetail,
            onMinusQuantity = { item ->
                if (item.quantity >= 2) {
                    val newList = salesDetailAdap.currentList().map {
                        if (it.productId == item.productId) {
                            val minusItem = it.quantity - 1
                            it.copy(
                                quantity = minusItem,
                                totalPrice = minusItem * it.unitPrice
                            )
                        } else it
                    }
                    Log.e("MENOS", newList.toString())
                    salesDetailAdap.updateList(newList)
                    productListSalesDetail = newList.toMutableList()
                    totalProducts(productListSalesDetail)
                }
            },
            onAddQueantity = { item ->
                val newList = salesDetailAdap.currentList().map {
                    if (it.productId == item.productId) {
                        val addItem = it.quantity + 1
                        it.copy(
                            quantity = addItem,
                            totalPrice = addItem * it.unitPrice
                        )
                    }else it
                }
                Log.e("MAS", newList.toString())
                salesDetailAdap.updateList(newList)
                productListSalesDetail = newList.toMutableList()
                totalProducts(productListSalesDetail)
            },
            onItemRemove = { itemToRemove ->
                val newList = salesDetailAdap.currentList().toMutableList().apply {
                    remove(itemToRemove)
                    productListSalesDetail.remove(itemToRemove)
                }
                salesDetailAdap.updateList(newList)
                totalProducts(productListSalesDetail)
            },
            onQuantityChanged = { item, newQuantity ->
                // Actualiza la lista con la nueva cantidad que el usuario escribió
                val newList = salesDetailAdap.currentList().map {
                    if (it.productId == item.productId)
                        it.copy(
                            quantity = newQuantity,
                            totalPrice = newQuantity * it.unitPrice
                        ) else it
                }
                Log.e("CHANGE", newList.toString())
                salesDetailAdap.updateList(newList)
                productListSalesDetail = newList.toMutableList()
                totalProducts(productListSalesDetail)
            }
        )

        attachSwipeToDelete(binding.rvSalesProductList)

        binding.rvSalesProductList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = salesDetailAdap
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun verifyQuantityProductList(productID:Int, quantitySales: Int): Boolean {
        var stock = true
        productList.map {

            if (it.id == productID){
                if(it.quantity == 0){
                    stock = false
                }

                if(quantitySales > it.quantity){
                    stock = false
                }

                /*if (quantitySales < it.quantity){
                    stock = true
                }*/
            }

        }
        return stock
    }

    private fun totalProducts(productList: MutableList<SalesDetailsModel>){
        var totalAccount = 0.00
        productList.map {
            totalAccount += it.totalPrice
        }

        binding.subTotalSalesFrag.text = String.format("%.2f", totalAccount)
        binding.totalSalesFrag.text = String.format("%.2f", totalAccount)

    }

    /*btn delete product list*/
    private fun attachSwipeToDelete(recyclerView: RecyclerView) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = salesDetailAdap.getItemAt(position)
                salesDetailAdap.onItemRemove(item)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val cornerRadius = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    5f,
                    recyclerView.context.resources.displayMetrics
                )

                val paint = Paint().apply {
                    color = Color.RED
                    isAntiAlias = true
                }

                val left = itemView.right + dX
                val top = itemView.top.toFloat()
                val right = itemView.right.toFloat()
                val bottom = itemView.bottom.toFloat()

                val rectF = RectF(left, top, right, bottom)
                c.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)

                val icon = ContextCompat.getDrawable(recyclerView.context, R.drawable.baseline_delete_24)!!
                val iconMargin = (itemView.height - icon.intrinsicHeight) / 2
                val iconTop = itemView.top + iconMargin
                val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                val iconBottom = iconTop + icon.intrinsicHeight

                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                icon.draw(c)

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)
    }

}