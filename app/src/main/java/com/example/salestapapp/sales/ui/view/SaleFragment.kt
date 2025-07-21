package com.example.salestapapp.sales.ui.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.salestapapp.sales.data.model.SalesModel
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
    private var productListSalesDetail: List<SalesDetailsModel> = emptyList()
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

        val sampleSalesDetails = listOf(
            SalesDetailsModel(
                id = 1,
                saleId = 1001,
                productId = 2001,
                productName = "Producto A",
                quantity = 2,
                unitPrice = 50.0,
                totalPrice = 2 * 50.0
            ),
            SalesDetailsModel(
                id = 2,
                saleId = 1001,
                productId = 2002,
                productName = "Producto B",
                quantity = 1,
                unitPrice = 100.0,
                totalPrice = 1 * 100.0
            ),
            SalesDetailsModel(
                id = 3,
                saleId = 1001,
                productId = 2003,
                productName = "Producto C",
                quantity = 3,
                unitPrice = 30.0,
                totalPrice = 3 * 30.0
            )
        )

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

        salesDetailAdap = ProductListSalesAdapter(
            sampleSalesDetails,
            onMinusQuantity = { item ->
                if (item.quantity >= 2) {
                    val newList = salesDetailAdap.currentList().map {
                        if (it.id == item.id) it.copy(quantity = it.quantity - 1) else it
                    }
                    salesDetailAdap.updateList(newList)
                }
            },
            onAddQueantity = { item ->
                val newList = salesDetailAdap.currentList().map {
                    if (it.id == item.id) it.copy(quantity = it.quantity + 1) else it
                }
                salesDetailAdap.updateList(newList)
            },
            onItemRemove = { itemToRemove ->
                val newList = salesDetailAdap.currentList().toMutableList().apply {
                    remove(itemToRemove)
                }
                salesDetailAdap.updateList(newList)
            },
            onQuantityChanged = { item, newQuantity ->
                // Actualiza la lista con la nueva cantidad que el usuario escribió
                val newList = salesDetailAdap.currentList().map {
                    if (it.id == item.id) it.copy(quantity = newQuantity) else it
                }
                salesDetailAdap.updateList(newList)
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
                val background = ColorDrawable(Color.RED)
                background.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
                background.draw(c)

                val icon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_delete_24)!!
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