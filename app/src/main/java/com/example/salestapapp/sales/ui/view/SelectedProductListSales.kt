package com.example.salestapapp.sales.ui.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.salestapapp.databinding.DialogProductListSalesBinding
import com.example.salestapapp.products.data.model.ProductModel
import com.example.salestapapp.sales.ui.SelectedProductListSalesAdapter

class SelectedProductListSales(
    private val context: Context,
    private val productList: List<ProductModel>,
    private val onSelectedProd: (ProductModel, Int) -> Unit,
    private val onCancel: () -> Unit = {}
) {

    private lateinit var dialog: Dialog
    private lateinit var binding: DialogProductListSalesBinding
    private var quantity = 0
    private lateinit var selectedProduct: ProductModel

    fun show() {
        dialog = Dialog(context)
        binding = DialogProductListSalesBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val adapterP = SelectedProductListSalesAdapter(
            productList,
            onClickItem = { product ->
                showQuantityInputDialog(context, product.quantity) { cantidad ->
                    Toast.makeText(context, "Cantidad ingresada: $cantidad", Toast.LENGTH_SHORT).show()
                    quantity = cantidad
                    selectedProduct = product
                    onSelectedProd(product, cantidad)
                    Log.e("PRODUCTO", selectedProduct.toString())
                    dialog.dismiss()
                }
            }
        )
        binding.rvProductsFragProductSales.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterP
        }

        binding.searchVProductSales.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapterP.filter(newText.orEmpty())
                return true
            }
        })

        binding.btnExitDialog.setOnClickListener {
            onCancel()
            dialog.dismiss()
        }

        dialog.setCancelable(true)
        dialog.show()
    }

    fun dismiss() {
        if (::dialog.isInitialized) dialog.dismiss()
    }

    private fun showQuantityInputDialog(
        context: Context,
        availableQuantity: Int, // <-- cantidad máxima permitida
        onAccept: (Int) -> Unit
    ) {
        val input = EditText(context).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
            hint = "Cantidad"
            setPadding(40, 40, 40, 40)

            background = GradientDrawable().apply {
                cornerRadius = 20f
                setStroke(2, Color.GRAY)
                setColor(Color.WHITE)
            }
        }

        val container = FrameLayout(context).apply {
            val margin = 32
            setPadding(margin, margin, margin, margin)
            addView(input)
        }

        val dialog = AlertDialog.Builder(context)
            .setTitle("Ingresa la cantidad")
            .setView(container)
            .setPositiveButton("Aceptar", null)
            .setNegativeButton("Cancelar") { d, _ -> d.dismiss() }
            .setCancelable(true)
            .create()

        dialog.show()

        dialog.window?.setBackgroundDrawable(
            GradientDrawable().apply {
                cornerRadius = 40f
                setColor(Color.WHITE)
            }
        )

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val valueStr = input.text.toString().trim()

            // Validaciones
            if (valueStr.isEmpty()) {
                input.error = "Por favor ingresa un número"
                return@setOnClickListener
            }

            val value = valueStr.toIntOrNull()
            if (value == null || value <= 0) {
                input.error = "Cantidad inválida"
                return@setOnClickListener
            }

            if (value > availableQuantity) {
                input.error = "Solo hay $availableQuantity disponibles"
                return@setOnClickListener
            }

            // Si pasa todas las validaciones
            onAccept(value)
            dialog.dismiss()
        }
    }

}