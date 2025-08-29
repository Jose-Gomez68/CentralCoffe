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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.salestapapp.R
import com.example.salestapapp.databinding.ActivityProductReportBinding
import com.example.salestapapp.products.data.ProductsRepository
import com.example.salestapapp.products.data.model.ProductModel
import com.example.salestapapp.reports.data.domain.usecase.GetMasVendidosUseCase
import com.example.salestapapp.reports.data.domain.usecase.GetProductReportInventUseCase
import com.example.salestapapp.reports.data.model.ProductosMasVendidosModel
import com.example.salestapapp.reports.ui.InventProductReportAdapter
import com.example.salestapapp.reports.ui.ProductReportMasAdap
import com.example.salestapapp.reports.ui.SalesReportAdapter
import com.example.salestapapp.reports.ui.viewmodel.GetInventReportViewModel
import com.example.salestapapp.reports.ui.viewmodel.GetInventReportViewModelFactory
import com.example.salestapapp.rom.CyberCoffeAppDatabase
import com.example.salestapapp.rom.CyberCoffeDatabase
import com.example.salestapapp.sales.data.SalesRepository
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

class ProductReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductReportBinding
    private lateinit var db: CyberCoffeDatabase
    private lateinit var viewModel: GetInventReportViewModel
    private lateinit var utilsFunctions: UtilsFunctions
    private var optionSelection = "Selecciona una Opción"
    private lateinit var prodAdap: InventProductReportAdapter
    private lateinit var masAdap: ProductReportMasAdap

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = CyberCoffeAppDatabase.CyberCoffeAppDatabase.getInstance(applicationContext)
        utilsFunctions = UtilsFunctions()

        val repository: ProductsRepository = ProductsRepository(db)
        val repositoryS: SalesRepository = SalesRepository(db)
        val viewModelProviderFactory = GetInventReportViewModelFactory(
            GetProductReportInventUseCase(repository),
            GetMasVendidosUseCase(repositoryS)
        )
        viewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        )[GetInventReportViewModel::class.java]

        spOptionReport()
        startDateSelected()
        endDateSelected()

        prodAdap = InventProductReportAdapter(emptyList())
        binding.rvProductReportInvet.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = prodAdap
        }

        masAdap = ProductReportMasAdap(emptyList())
        binding.rvProductReportMas.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = masAdap
        }

        binding.btnReturnReportProductReport.setOnClickListener {
            onBackPressed()
        }

        binding.btnReportProductReport.setOnClickListener {
            binding.btnExportarInventProductReport.visibility = View.GONE
            binding.lyHeaderProductReport.visibility = View.VISIBLE
            if (optionSelection.equals("Inventario")) {
                binding.lyHeaderInventReportProduct.visibility = View.VISIBLE
                binding.lyReportMasVendidosProductReport.visibility = View.GONE
                binding.rvProductReportInvet.visibility = View.VISIBLE
                binding.rvProductReportMas.visibility = View.GONE
                viewModel.invoke()
                binding.btnExportarInventProductReport.visibility = View.VISIBLE

            } else if(optionSelection.equals("Productos Más Vendidos")) {
                binding.lyReportMasVendidosProductReport.visibility = View.VISIBLE
                binding.lyHeaderInventReportProduct.visibility = View.GONE
                binding.rvProductReportMas.visibility = View.VISIBLE
                binding.rvProductReportInvet.visibility = View.GONE
                if (validationForm()) {
                    viewModel.masVendidos(
                        binding.etReportDateProductReport.text.toString(),
                        binding.etReportDate2ProductReport.text.toString()
                    )
                }
                //binding.btnExportarInventProductReport.visibility = View.VISIBLE
            }
        }

        /**FALTA CREAR LOS ADAPTER PARA CADA LISTADO*/
        viewModel.getReportInvent.observe(this) { report ->
            prodAdap.updateLis(report)
        }

        viewModel.getMas.observe(this) { report ->
            masAdap.updateListMas(report)
        }

        binding.btnExportarInventProductReport.setOnClickListener {
            var report: List<ProductModel> = emptyList()
            var report2: List<ProductosMasVendidosModel> = emptyList()
            if (optionSelection.equals("Inventario"))
                report = viewModel.getReportInvent.value!!
            else if (optionSelection.equals("Productos Más Vendidos"))
                report2 = viewModel.getMas.value!!

            showExportDialog(
                "Exportando a Excel",
                "El archivo se exportará en la carpeta de descargas o download",
                onExport = {
                    if (optionSelection.equals("Inventario")) {
                        if (report != null) {
                            exportToExcelInventario(report)
                        } else {
                            Toast.makeText(this, "No hay datos para exportar", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }else if(optionSelection.equals("Productos Más Vendidos")){
                        if (report2 != null) {
                            exportToExcelProductosMasVendidos(report2, binding.etReportDateProductReport.text.toString(),
                                binding.etReportDate2ProductReport.text.toString())
                        } else {
                            Toast.makeText(this, "No hay datos para exportar", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                },
                onCancel = {
                    // nada o cerrar
                },
                onShare = {
                    if (optionSelection.equals("Inventario")) {
                        if (report != null) {
                            val file =
                                exportToExcelInventario(report) // devuelve el archivo generado
                            shareFile(this, file!!)
                        } else {
                            Toast.makeText(this, "No hay datos para compartir", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }else if(optionSelection.equals("Productos Más Vendidos")) {
                        if (report2 != null) {
                            val file =
                                exportToExcelProductosMasVendidos(report2, binding.etReportDateProductReport.text.toString(),
                                    binding.etReportDate2ProductReport.text.toString()) // devuelve el archivo generado
                            shareFile(this, file!!)
                        } else {
                            Toast.makeText(this, "No hay datos para compartir", Toast.LENGTH_SHORT)
                                .show()
                        }
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
            binding.etReportDateProductReport.setText(sdf.format(myCalendar.time))
        }

        binding.etReportDateProductReport.setOnClickListener {
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
            binding.etReportDate2ProductReport.setText(sdf.format(myCalendar.time))
        }

        binding.etReportDate2ProductReport.setOnClickListener {
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
        if (binding.etReportDateProductReport.text.toString().isEmpty()){
            binding.etReportDateProductReport.error = etEmpty
            return false
        }else if (binding.etReportDate2ProductReport.text.toString().isEmpty()){
            binding.etReportDate2ProductReport.error = etEmpty2
            return false
        }

        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val fechaInicio = sdf.parse(binding.etReportDateProductReport.text.toString())
            val fechaFin = sdf.parse(binding.etReportDate2ProductReport.text.toString())

            if (fechaInicio != null && fechaFin != null && fechaInicio.after(fechaFin)) {
                binding.etReportDateProductReport.error = "La fecha inicial debe ser menor o igual a la final"
                binding.etReportDate2ProductReport.error = "La fecha final debe ser mayor o igual a la inicial"
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al leer las fechas", Toast.LENGTH_SHORT).show()
            return false
        }

        binding.etReportDateProductReport.error = null
        binding.etReportDate2ProductReport.error = null
        binding.btnExportarInventProductReport.visibility = View.VISIBLE

        return true

    }


    private fun spOptionReport() {
        val opciones = listOf(
            "Selecciona una Opción",
            "Inventario",
            "Productos Más Vendidos",
            "Productos Menos Vendidos"
        )

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

                if (optionSelection.equals("Inventario")) {
                    binding.etReportDateProductReport.visibility = View.GONE
                    binding.etReportDate2ProductReport.visibility = View.GONE
                    viewModel.invoke()

                } else if(optionSelection.equals("Productos Más Vendidos")) {
                    binding.etReportDateProductReport.visibility = View.VISIBLE
                    binding.etReportDate2ProductReport.visibility = View.VISIBLE

                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //no hacer nada
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun exportToExcelInventario(productList: List<ProductModel>): Uri? {
        val wb = XSSFWorkbook()
        val sheet = wb.createSheet("Reporte de Inventario")

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
        val headerStyle = bordered().apply {
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
        sheet.addMergedRegion(CellRangeAddress(0,0,0,7)) // 8 columnas ahora
        val titleRow = sheet.createRow(0)
        titleRow.heightInPoints = 28f
        titleRow.createCell(0).apply {
            setCellValue("REPORTE DE INVENTARIO")
            cellStyle = titleStyle
        }

        // ===== Cabeceras =====
        val headerRow = sheet.createRow(2)
        val headers = listOf(
            "#", "Cod Producto", "Producto", "Categoria", "Stock",
            "Precio Unit", "Valor Inventario", "Ultima Actualizacion del Producto"
        )
        headers.forEachIndexed { i,h ->
            headerRow.createCell(i).apply {
                setCellValue(h)
                cellStyle = headerStyle as XSSFCellStyle?
            }
        }

        // ===== Filas de productos =====
        var rowIdx = 3
        var totalInventario = 0.0
        productList.forEachIndexed { index, p ->
            val r = sheet.createRow(rowIdx++)
            val valorInventario = p.quantity * p.price
            totalInventario += valorInventario

            r.createCell(0).apply { setCellValue((index + 1).toDouble()); cellStyle = textStyle as XSSFCellStyle? } // #
            r.createCell(1).apply { setCellValue(p.id.toString()); cellStyle = textStyle as XSSFCellStyle? }
            r.createCell(2).apply { setCellValue(p.name); cellStyle = textStyle as XSSFCellStyle? }
            r.createCell(3).apply { setCellValue(p.category); cellStyle = textStyle as XSSFCellStyle? }
            r.createCell(4).apply { setCellValue(p.quantity.toString()); cellStyle = textStyle as XSSFCellStyle? }
            r.createCell(5).apply { setCellValue(p.price); cellStyle = moneyStyle as XSSFCellStyle? }
            r.createCell(6).apply { setCellValue(valorInventario); cellStyle = moneyStyle as XSSFCellStyle? }
            r.createCell(7).apply { setCellValue(p.updateDate); cellStyle = textStyle as XSSFCellStyle? }
        }

        // ===== Fila Total =====
        val totalRow = sheet.createRow(rowIdx + 1)
        totalRow.createCell(5).apply {
            setCellValue("TOTAL INVENTARIO")
            cellStyle = headerStyle as XSSFCellStyle?
        }
        totalRow.createCell(6).apply {
            setCellValue(totalInventario)
            cellStyle = moneyStyle as XSSFCellStyle?
        }

        // ===== Ajustar ancho columnas =====
        headers.indices.forEach { i -> sheet.setColumnWidth(i, 18 * 256) }

        // ===== Guardar a Descargas =====
        val fileName = "ReporteInventario_${System.currentTimeMillis()}.xlsx"
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

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun exportToExcelProductosMasVendidos(
        productReportList: List<ProductosMasVendidosModel>,
        startDate: String,
        endDate: String
    ): Uri? {
        val wb = XSSFWorkbook()
        val sheet = wb.createSheet("Productos Más Vendidos")

        // ===== Formatos =====
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
        val headerStyle = bordered().apply {
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
        sheet.addMergedRegion(CellRangeAddress(0, 0, 0, 4)) // ahora 5 columnas
        val titleRow = sheet.createRow(0)
        titleRow.heightInPoints = 28f
        titleRow.createCell(0).apply {
            setCellValue("REPORTE DE PRODUCTOS MÁS VENDIDOS")
            cellStyle = titleStyle as XSSFCellStyle?
        }

        // ===== Periodo ===== (solo fecha)
        val sdfInput = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US)
        val sdfOutput = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val periodoRow = sheet.createRow(1)
        periodoRow.createCell(0).apply {
            setCellValue(
                "Periodo: ${sdfOutput.format(sdfInput.parse(startDate)!!)} a ${sdfOutput.format(sdfInput.parse(endDate)!!)}"
            )
            cellStyle = textStyle as XSSFCellStyle?
        }
        sheet.addMergedRegion(CellRangeAddress(1, 1, 0, 4))

        // ===== Cabeceras =====
        val headerRow = sheet.createRow(3)
        val headers = listOf("ID Producto", "Nombre", "Categoría", "Cantidad Vendida", "Total Ingresos")
        headers.forEachIndexed { i, h ->
            headerRow.createCell(i).apply {
                setCellValue(h)
                cellStyle = headerStyle as XSSFCellStyle?
            }
        }

        // ===== Filas de productos =====
        var rowIdx = 4
        var totalIngresosGeneral = 0.0
        productReportList.forEach { p ->
            val r = sheet.createRow(rowIdx++)
            r.createCell(0).apply { setCellValue(p.productId.toDouble()); cellStyle = textStyle as XSSFCellStyle? }
            r.createCell(1).apply { setCellValue(p.productName); cellStyle = textStyle as XSSFCellStyle? }
            r.createCell(2).apply { setCellValue(p.category); cellStyle = textStyle as XSSFCellStyle? }
            r.createCell(3).apply { setCellValue(p.totalVendidos.toDouble()); cellStyle = textStyle as XSSFCellStyle? }
            r.createCell(4).apply { setCellValue(p.totalIngresos); cellStyle = moneyStyle as XSSFCellStyle? }

            totalIngresosGeneral += p.totalIngresos
        }

        // ===== Total ingresos =====
        val totalRow = sheet.createRow(rowIdx++)
        totalRow.createCell(3).apply { setCellValue("TOTAL:"); cellStyle = textStyle as XSSFCellStyle? }
        totalRow.createCell(4).apply { setCellValue(totalIngresosGeneral); cellStyle = moneyStyle as XSSFCellStyle? }

        // ===== Ajustar ancho columnas =====
        sheet.setColumnWidth(0, 15 * 256) // ID Producto
        sheet.setColumnWidth(1, 30 * 256) // Nombre
        sheet.setColumnWidth(2, 20 * 256) // Categoría
        sheet.setColumnWidth(3, 15 * 256) // Cantidad Vendida
        sheet.setColumnWidth(4, 20 * 256) // Total Ingresos

        // ===== Guardar a Descargas =====
        val fileName = "ReporteMasVendidos_${System.currentTimeMillis()}.xlsx"
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

}