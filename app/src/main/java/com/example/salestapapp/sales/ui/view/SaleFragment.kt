package com.example.salestapapp.sales.ui.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
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
import android.widget.ArrayAdapter
import android.widget.Spinner
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
import com.example.salestapapp.sales.data.domain.GetSalesWithDetailsUseCase
import com.example.salestapapp.sales.data.domain.InsertSalesUseCase
import com.example.salestapapp.sales.data.domain.UpdateStockProductUseCase
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
            GetSalesWithDetailsUseCase(repository),
            GetProductsUseCase(repositoryProduct),
            UpdateStockProductUseCase(repositoryProduct)
        )

        viewModel = ViewModelProvider(
            this, viewModelProviderFactory
        )[NewSalesViewModel::class.java]

        viewModel.getALlProducts()

        binding.btnReturnSalesFrag.setOnClickListener {
            util.showConfirmDialog(requireActivity(),
                getString(R.string.title_message_return_view_util),
                getString(R.string.message_return_view_util),
                getString(R.string.exit_message_return_view_util),
                getString(R.string.cancel_message_return_view_util),
                onConfirm = {
                    requireActivity().onBackPressed()
                },
                onCancel = {

                }
            )
        }

        viewModel.productModel.observe(viewLifecycleOwner) { result ->

            productList = result

        }

        binding.btnAddProductListSales.setOnClickListener {
            val dialog = SelectedProductListSales(
                context = requireContext(),
                productList = productList,
                onSelectedProd = { selectProd, quantity ->
                    val existingProduct = productListSalesDetail.find { it.productId == selectProd.id }
                    if (existingProduct != null && verifyQuantityProductList(selectProd.id, existingProduct!!.quantity + quantity)){
                        existingProduct.quantity += quantity
                        existingProduct.totalPrice = existingProduct.quantity*existingProduct.unitPrice
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
                        }
                    }

                    totalProducts(productListSalesDetail)

                },
                onCancel = {
                }
            )
            dialog.show()
        }


/*VERIFICAR LAS CANTIDADES CON LOS BOTONES DE DISMINUIR Y AUMENTAR*/
        salesDetailAdap = ProductListSalesAdapter(
            productListSalesDetail,
            onMinusQuantity = { item ->
                val currentItem = salesDetailAdap.currentList()
                    .firstOrNull { it.productId == item.productId }

                // Aquí validas que ya sea mayor a 1 antes de restar
                if (currentItem != null && currentItem.quantity > 1) {
                    val minusItem = (currentItem.quantity - 1).coerceAtLeast(1) // nunca menos de 1

                    val newList = salesDetailAdap.currentList().map {
                        if (it.productId == item.productId) {
                            it.copy(
                                quantity = minusItem,
                                totalPrice = minusItem * it.unitPrice
                            )
                        } else it
                    }
                    salesDetailAdap.updateList(newList)
                    productListSalesDetail = newList.toMutableList()
                    totalProducts(productListSalesDetail)
                }
            },
            onAddQueantity = { item ->
                val prod = productList.find { it.id == item.productId }
                val currentItem = salesDetailAdap.currentList().find { it.productId == item.productId }

                if (prod != null && currentItem != null && prod.quantity > currentItem.quantity) {
                    val newList = salesDetailAdap.currentList().map {
                        if (it.productId == item.productId) {
                            val addItem = it.quantity + 1
                            it.copy(
                                quantity = addItem,
                                totalPrice = addItem * it.unitPrice
                            )
                        } else it
                    }

                    salesDetailAdap.updateList(newList)
                    productListSalesDetail = newList.toMutableList()
                    totalProducts(productListSalesDetail)
                }

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
                val prod = productList.find {
                    it.id == item.productId

                }

                Log.e("AQUI", prod!!.name.toString())
                Log.e("AQUI", prod!!.quantity.toString())
                Log.e("AQUI", item.quantity.toString())

                //if (prod!!.quantity > newQuantity)
                // Actualiza la lista con la nueva cantidad que el usuario escribió
                val newList = salesDetailAdap.currentList().map {
                    if (it.productId == item.productId)
                        it.copy(
                            quantity = newQuantity,
                            totalPrice = newQuantity * it.unitPrice
                        ) else it
                }
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

        binding.btnSaveSale.setOnClickListener {
            if (validationsForm()){
                showPaymentDialog(requireContext())

            }
        }

        viewModel.insertResult.observe(viewLifecycleOwner) { saleId ->
           /* if (result){
                util.generatePosTicket()
                binding.subTotalSalesFrag.setText("0")
                binding.totalSalesFrag.setText("0")
                salesDetailAdap.updateList(emptyList())
                productListSalesDetail = emptyList<SalesDetailsModel>().toMutableList()
            }*/
            if (saleId != null && saleId > 0) {
                viewModel.fetchSaleWithDetails(saleId)
            } else {
                Toast.makeText(requireContext(), "Error al guardar la venta", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.saleModel.observe(viewLifecycleOwner) { sale ->
            if (sale != null) {
                sale.salesDetails.map { product ->
                    var prodSelectList = productList.find { it.id == product.productId }
                    viewModel.updateStockProduct(product.productId, prodSelectList!!.quantity - product.quantity)
                }
                val ticket = util.generatePosTicket(
                    orderId = sale.sales.id.toString(),
                    items = sale.salesDetails.map { Triple(it.quantity, it.productName, it.totalPrice) },
                    total = sale.sales.total
                )

                util.printViaTcpIp(requireContext(), ticket, "192.168.1.240")
                //showAfterPrintDialog(requireContext(), ticket)
                showPrintDialog(requireContext(), ticket,
                    reprintAction = {
                        util.printViaTcpIp(requireContext(), ticket, "192.168.1.240")
                    }
                )

                binding.subTotalSalesFrag.text = "0"
                binding.totalSalesFrag.text = "0"
                salesDetailAdap.updateList(emptyList())
                productListSalesDetail = mutableListOf()
                viewModel.getALlProducts()
            } else {
                Toast.makeText(requireContext(), "Error al cargar la venta", Toast.LENGTH_SHORT).show()
            }
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

    private fun showPaymentDialog(context: Context) {
        val paymentMethods = listOf("EFECTIVO", "TARJETA DÉBITO", "TARJETA CRÉDITO", "TRANSFERENCIA")

        // Crear el spinner programáticamente
        val spinner = Spinner(context).apply {
            adapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                paymentMethods
            )
        }

        // Construir el diálogo
        AlertDialog.Builder(context)
            .setTitle("Selecciona método de pago")
            .setView(spinner)
            .setPositiveButton("Aceptar") { dialog, _ ->
                val selectedMethod = spinner.selectedItem.toString()
                Toast.makeText(context, "Seleccionaste: $selectedMethod", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                val sale = SalesModel(
                    0,
                    binding.subTotalSalesFrag.text.toString().toDouble(),
                    binding.totalSalesFrag.text.toString().toDouble(),
                    "COMPLETADA",
                    selectedMethod,
                    util.getCurrentFormattedDate()
                )


                viewModel.onCreate(sale, productListSalesDetail)
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    private fun validationsForm(): Boolean {
        if(productListSalesDetail.isEmpty())
            return false

        return true
    }

    fun showPrintDialog(context: Context, ticket: String, reprintAction: () -> Unit) {
        AlertDialog.Builder(context)
            .setTitle("Impresión finalizada")
            .setMessage("¿Qué desea hacer?")
            .setPositiveButton("Reimprimir") { _, _ ->
                reprintAction()
            }
            .setNeutralButton("Compartir") { _, _ ->
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, ticket)
                }
                context.startActivity(Intent.createChooser(shareIntent, "Compartir ticket"))
            }
            .setNegativeButton("Salir") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
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