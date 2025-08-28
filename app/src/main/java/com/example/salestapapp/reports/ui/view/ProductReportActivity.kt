package com.example.salestapapp.reports.ui.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.R
import com.example.salestapapp.databinding.ActivityProductReportBinding
import com.example.salestapapp.databinding.ActivityReportBinding
import com.example.salestapapp.databinding.ActivitySalesReportBinding
import com.example.salestapapp.products.data.ProductsRepository
import com.example.salestapapp.reports.data.domain.usecase.GetCashCutReportUseCase
import com.example.salestapapp.reports.data.domain.usecase.GetProductReportInventUseCase
import com.example.salestapapp.reports.ui.viewmodel.CashCutReportViewModel
import com.example.salestapapp.reports.ui.viewmodel.CashCutReportViewModelFactory
import com.example.salestapapp.reports.ui.viewmodel.GetInventReportViewModel
import com.example.salestapapp.reports.ui.viewmodel.GetInventReportViewModelFactory
import com.example.salestapapp.rom.CyberCoffeAppDatabase
import com.example.salestapapp.rom.CyberCoffeDatabase
import com.example.salestapapp.sales.data.SalesRepository
import com.example.salestapapp.util.UtilsFunctions

class ProductReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductReportBinding
    private lateinit var db: CyberCoffeDatabase
    private lateinit var viewModel: GetInventReportViewModel
    private lateinit var utilsFunctions: UtilsFunctions
    private var optionSelection = "Selecciona una Opción"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = CyberCoffeAppDatabase.CyberCoffeAppDatabase.getInstance(applicationContext)
        utilsFunctions = UtilsFunctions()

        val repository: ProductsRepository = ProductsRepository(db)
        val viewModelProviderFactory = GetInventReportViewModelFactory(
            GetProductReportInventUseCase(repository)
        )
        viewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        )[GetInventReportViewModel::class.java]

        spOptionReport()

        binding.btnReportProductReport.setOnClickListener {
            binding.lyHeaderProductReport.visibility = View.VISIBLE
            if (optionSelection.equals("Inventario")) {
                binding.lyHeaderInventReportProduct.visibility = View.VISIBLE
                binding.lyReportMasVendidosProductReport.visibility = View.GONE
                viewModel.invoke()
            } else if(optionSelection.equals("Productos Más Vendidos")) {
                binding.lyReportMasVendidosProductReport.visibility = View.VISIBLE
                binding.lyHeaderInventReportProduct.visibility = View.GONE
            }
        }

        /**FALTA CREAR LOS ADAPTER PARA CADA LISTADO*/
        viewModel.getReportInvent.observe(this) { report ->
            Log.e("AQUII", ""+report)
        }

    }

    private fun spOptionReport() {
        val opciones = listOf(
            "Selecciona una Opción",
            "Inventario",
            "Productos Más Vendidos",
            "Productos Menos Vendidos"
        )

// Crear el adaptador
        val adapter = ArrayAdapter(
            this, // si es en un Fragment usa requireContext()
            android.R.layout.simple_spinner_item,
            opciones
        )
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.spSelectProductReport.adapter = adapter
        binding.spSelectProductReport.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                optionSelection = opciones[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //no hacer nada
            }

        }
    }

}