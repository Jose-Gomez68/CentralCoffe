package com.example.salestapapp.reports.ui.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import com.example.salestapapp.sales.data.model.SalesModel
import com.example.salestapapp.util.UtilsFunctions
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

class SalesReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySalesReportBinding
    private lateinit var viewModel: SalesReportViewModel
    private lateinit var db: CyberCoffeDatabase
    private lateinit var utilsFunctions: UtilsFunctions
    private lateinit var saleReportAdap: SalesReportAdapter

    @RequiresApi(Build.VERSION_CODES.Q)
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
            if (validationForm()) {
                viewModel.invoke(
                    binding.etReportDateSalesReport.text.toString(),
                    binding.etReportDate2SalesReport.text.toString()
                )
                binding.scvTableSalesReport.visibility = View.VISIBLE
                binding.lyHeaderSalesReport.visibility = View.VISIBLE
            }
        }

        viewModel.getSalesReportt.observe(this) { report ->
            saleReportAdap.updateLis(report)
        }

        binding.btnReturnReportSalesReport.setOnClickListener {
            onBackPressed()
        }

        binding.btnExportarSalesReport.setOnClickListener {

            val report = viewModel.getSalesReportt.value

            showExportDialog(
                "Exportando a Excel",
                "El archivo se exportará en la carpeta de descargas o download",
                onExport = {
                    if (report != null) {
                        exportToExcelVentas(report)
                    } else {
                        Toast.makeText(this, "No hay datos para exportar", Toast.LENGTH_SHORT).show()
                    }
                },
                onCancel = {
                    // nada o cerrar
                },
                onShare = {
                    if (report != null) {
                        val file = exportToExcelVentas(report) // devuelve el archivo generado
                        shareFile(this, file!!)
                    } else {
                        Toast.makeText(this, "No hay datos para compartir", Toast.LENGTH_SHORT).show()
                    }
                }
            )
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

    private fun validationForm(): Boolean {
        val etEmpty = "Selecciona la Fecha Inicial"
        val etEmpty2 = "Selecciona la Fecha Final"
        if (binding.etReportDateSalesReport.text.toString().isEmpty()){
            binding.etReportDateSalesReport.error = etEmpty
            return false
        }else if (binding.etReportDate2SalesReport.text.toString().isEmpty()){
            binding.etReportDate2SalesReport.error = etEmpty2
            return false
        }

        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val fechaInicio = sdf.parse(binding.etReportDateSalesReport.text.toString())
            val fechaFin = sdf.parse(binding.etReportDate2SalesReport.text.toString())

            if (fechaInicio != null && fechaFin != null && fechaInicio.after(fechaFin)) {
                binding.etReportDateSalesReport.error = "La fecha inicial debe ser menor o igual a la final"
                binding.etReportDate2SalesReport.error = "La fecha final debe ser mayor o igual a la inicial"
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al leer las fechas", Toast.LENGTH_SHORT).show()
            return false
        }

        binding.etReportDateSalesReport.error = null
        binding.etReportDate2SalesReport.error = null
        binding.btnExportarSalesReport.visibility = View.VISIBLE

        return true

    }

    private fun showExportDialog(
        title: String,
        message: String,
        onExport: () -> Unit,
        onCancel: () -> Unit,
        onShare: () -> Unit
    ) {
        val dialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Exportar") { _, _ ->
                onExport()
            }
            .setNegativeButton("Cancelar") { _, _ ->
                onCancel()
            }
            .setNeutralButton("Compartir") { _, _ ->
                onShare()
            }
            .create()

        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun exportToExcelVentas(salesList: List<SalesModel>): Uri? {
        val wb = XSSFWorkbook()
        val sheet = wb.createSheet("Reporte de Ventas")

        // ===== Formatos y estilos =====
        val df = wb.createDataFormat()
        val moneyFmt = df.getFormat("$#,##0.00")

        fun bordered(base: CellStyle = wb.createCellStyle()): CellStyle = base.apply {
            borderTop = BorderStyle.THIN
            borderBottom = BorderStyle.THIN
            borderLeft = BorderStyle.THIN
            borderRight = BorderStyle.THIN
            verticalAlignment = VerticalAlignment.CENTER
        }

        val titleFont = wb.createFont().apply {
            bold = true
            fontHeightInPoints = 16
            color = IndexedColors.WHITE.index
        }
        val titleStyle = wb.createCellStyle().apply {
            setFillForegroundColor(IndexedColors.DARK_BLUE.index)
            fillPattern = FillPatternType.SOLID_FOREGROUND
            alignment = HorizontalAlignment.CENTER
            setFont(titleFont)
        }

        val headerFont = wb.createFont().apply { bold = true; color = IndexedColors.WHITE.index }
        val salesHeaderStyle = bordered().apply {
            setFillForegroundColor(IndexedColors.DARK_TEAL.index)
            fillPattern = FillPatternType.SOLID_FOREGROUND
            alignment = HorizontalAlignment.CENTER
            setFont(headerFont)
        }

        val moneyStyle = bordered().apply {
            alignment = HorizontalAlignment.RIGHT
            dataFormat = moneyFmt
        }
        val textStyle = bordered().apply { alignment = HorizontalAlignment.CENTER }

        // ===== Título =====
        sheet.addMergedRegion(CellRangeAddress(0,0,0,6)) // 7 columnas ahora
        val titleRow = sheet.createRow(0)
        titleRow.heightInPoints = 28f
        titleRow.createCell(0).apply {
            setCellValue("REPORTE DE VENTAS")
            cellStyle = titleStyle
        }

        // ===== Periodo =====
        sheet.addMergedRegion(CellRangeAddress(1,1,0,6))
        val periodoRow = sheet.createRow(1)
        val fechaInicio = binding.etReportDateSalesReport.text.toString()
        val fechaFin = binding.etReportDate2SalesReport.text.toString()
        periodoRow.createCell(0).apply {
            setCellValue("Periodo: $fechaInicio - $fechaFin")
            cellStyle = textStyle as XSSFCellStyle?
        }

        // ===== Cabeceras =====
        val headerRow = sheet.createRow(3)
        val headers = listOf("#", "# Venta", "SubTotal", "Total", "Estatus", "Método", "Fecha")
        headers.forEachIndexed { i,h ->
            headerRow.createCell(i).apply {
                setCellValue(h)
                cellStyle = salesHeaderStyle as XSSFCellStyle?
            }
        }

        // ===== Filas de ventas =====
        var rowIdx = 4
        salesList.forEachIndexed { index, s ->
            val r = sheet.createRow(rowIdx++)
            r.createCell(0).apply { setCellValue((index + 1).toDouble()); cellStyle = textStyle as XSSFCellStyle? } // Índice
            r.createCell(1).apply { setCellValue(s.id.toDouble()); cellStyle = textStyle as XSSFCellStyle? }
            r.createCell(2).apply { setCellValue(s.subTotal); cellStyle = moneyStyle as XSSFCellStyle? }
            r.createCell(3).apply { setCellValue(s.total); cellStyle = moneyStyle as XSSFCellStyle? }
            r.createCell(4).apply { setCellValue(s.statusSales); cellStyle = textStyle as XSSFCellStyle? }
            r.createCell(5).apply { setCellValue(s.paymentMethod); cellStyle = textStyle as XSSFCellStyle? }
            r.createCell(6).apply { setCellValue(s.createDate); cellStyle = textStyle as XSSFCellStyle? }
        }

        // ===== Ajustar ancho columnas =====
        sheet.setColumnWidth(0, 8 * 256)  // Índice
        sheet.setColumnWidth(1, 10 * 256) // # Venta
        sheet.setColumnWidth(2, 12 * 256) // SubTotal
        sheet.setColumnWidth(3, 12 * 256) // Total
        sheet.setColumnWidth(4, 15 * 256) // Estatus
        sheet.setColumnWidth(5, 15 * 256) // Método
        sheet.setColumnWidth(6, 20 * 256) // Fecha

        // ===== Guardar a Descargas =====
        val fileName = "ReporteVentas_${System.currentTimeMillis()}.xlsx"
        return try {
            val bos = ByteArrayOutputStream()
            wb.write(bos)
            wb.close()

            val bytes = bos.toByteArray()
            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                put(MediaStore.Downloads.IS_PENDING, 1)
            }

            val resolver = contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                resolver.openOutputStream(uri)?.use { it.write(bytes) }
                values.clear()
                values.put(MediaStore.Downloads.IS_PENDING, 0)
                resolver.update(uri, values, null, null)
                Toast.makeText(this, "Exportado a Descargas: $fileName", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "No se pudo crear el archivo en Descargas", Toast.LENGTH_LONG).show()
            }
            uri
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al exportar: ${e.message}", Toast.LENGTH_LONG).show()
            null
        }
    }


    private fun shareFile(context: Context, uri: Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/vnd.ms-excel"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Compartir Excel con:"))
    }

}