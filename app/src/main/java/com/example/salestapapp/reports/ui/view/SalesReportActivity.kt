package com.example.salestapapp.reports.ui.view

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.salestapapp.databinding.ActivitySalesReportBinding
import com.example.salestapapp.reports.data.domain.usecase.GetSalesReportUseCase
import com.example.salestapapp.reports.ui.SalesReportAdapter
import com.example.salestapapp.reports.ui.viewmodel.SalesReportViewModel
import com.example.salestapapp.reports.ui.viewmodel.SalesReportViewModelFactory
import com.example.salestapapp.rom.CyberCoffeAppDatabase
import com.example.salestapapp.rom.CyberCoffeDatabase
import com.example.salestapapp.sales.data.SalesRepository
import com.example.salestapapp.util.UtilsFunctions
import java.text.SimpleDateFormat
import java.util.Locale

class SalesReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySalesReportBinding
    private lateinit var viewModel: SalesReportViewModel
    private lateinit var db: CyberCoffeDatabase
    private lateinit var utilsFunctions: UtilsFunctions
    private lateinit var saleReportAdap: SalesReportAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalesReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = CyberCoffeAppDatabase.CyberCoffeAppDatabase.getInstance(applicationContext)
        utilsFunctions = UtilsFunctions()
        val repository: SalesRepository = SalesRepository(db)
        val viewModelProviderFactory = SalesReportViewModelFactory(
            GetSalesReportUseCase(repository)
        )
        viewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        )[SalesReportViewModel::class.java]

        startDateSelected()
        endDateSelected()

        saleReportAdap = SalesReportAdapter(emptyList())
        binding.rvSalesReportList.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = saleReportAdap
        }

        binding.btnReporSalesReport.setOnClickListener {
            viewModel.invoke(binding.etReportDateSalesReport.text.toString(), binding.etReportDate2SalesReport.text.toString())
        }

        viewModel.getSalesReportt.observe(this) { report ->
            Log.e("AQUIIII", ""+report)
            saleReportAdap.updateLis(report)
        }

    }

    private fun startDateSelected() {
        val myCalendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, day)
            // Ajustar hora al inicio del día
            myCalendar.set(Calendar.HOUR_OF_DAY, 0)
            myCalendar.set(Calendar.MINUTE, 0)
            myCalendar.set(Calendar.SECOND, 0)
            myCalendar.set(Calendar.MILLISECOND, 0)

            val myFormat = "dd/MM/yyyy HH:mm:ss"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            binding.etReportDateSalesReport.setText(sdf.format(myCalendar.time))
        }

        binding.etReportDateSalesReport.setOnClickListener {
            DatePickerDialog(
                this,
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun endDateSelected() {
        val myCalendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, day)
            // Ajustar hora al final del día
            myCalendar.set(Calendar.HOUR_OF_DAY, 23)
            myCalendar.set(Calendar.MINUTE, 59)
            myCalendar.set(Calendar.SECOND, 59)
            myCalendar.set(Calendar.MILLISECOND, 999)

            val myFormat = "dd/MM/yyyy HH:mm:ss"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            binding.etReportDate2SalesReport.setText(sdf.format(myCalendar.time))
        }

        binding.etReportDate2SalesReport.setOnClickListener {
            DatePickerDialog(
                this,
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }


}