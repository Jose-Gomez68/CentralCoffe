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
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.databinding.ActivityCashCutBinding
import com.example.salestapapp.reports.data.domain.usecase.GetCashCutReportUseCase
import com.example.salestapapp.reports.data.model.CashCutModel
import com.example.salestapapp.reports.ui.viewmodel.CashCutReportViewModel
import com.example.salestapapp.reports.ui.viewmodel.CashCutReportViewModelFactory
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

class CashCutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCashCutBinding
    private var turnoSelected = "Seleccione un Turno"
    private lateinit var db: CyberCoffeDatabase
    private lateinit var viewModel: CashCutReportViewModel
    private lateinit var utilsFunctions: UtilsFunctions
    private var startTime = ""
    private var endTime = ""

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCashCutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = CyberCoffeAppDatabase.CyberCoffeAppDatabase.getInstance(applicationContext)
        utilsFunctions = UtilsFunctions()
        val repository: SalesRepository = SalesRepository(db)
        val viewModelProviderFactory = CashCutReportViewModelFactory(
            GetCashCutReportUseCase(repository)
        )
        viewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        )[CashCutReportViewModel::class.java]

        spinnerTurno()
        dateSelected()

        binding.btnReportCaja.setOnClickListener {
            if (validationForm()) {
                if (turnoSelected.equals("Mañana")) {
                    startTime = "07:00:00"
                    endTime = "14:00:00"
                } else if (turnoSelected.equals("Tarde")) {
                    startTime = "14:01:00"
                    endTime = "21:00:00"
                }
                viewModel.invoke(binding.etReportDateCashCut.text.toString(), startTime, endTime)
            }
        }

        viewModel.getCashCutL.observe(this) { report ->
            visibleComponent()
            val credito = report.ttCredito ?: 0.0
            val debito = report.ttDebito ?: 0.0
            val transfer = report.ttTransfer ?: 0.0
            val efectivo = report.ttEfective ?: 0.0

            binding.tvCreditPayReportCashCut.text = credito.toString()
            binding.tvDebitCardPayReportCashCut.text = debito.toString()
            binding.tvTransferReportCashCut.text = transfer.toString()
            binding.tvEfectiReportCashCut.text = efectivo.toString()

            val total = credito + debito + transfer + efectivo
            binding.tvTotalReportCashCut.text = total.toString()
        }

        viewModel.getSales.observe(this) { salesReport ->

        }

        binding.btnExportar.setOnClickListener {
            val report = viewModel.getCashCutL.value
            val reportSalesCut = viewModel.getSales.value

            showExportDialog(
                "Exportando a Excel",
                "El archivo se exportará en la carpeta de descargas o download",
                onExport = {
                    if (report != null) {
                        exportToExcel(report, reportSalesCut!!)
                    } else {
                        Toast.makeText(this, "No hay datos para exportar", Toast.LENGTH_SHORT).show()
                    }
                },
                onCancel = {
                    // nada o cerrar
                },
                onShare = {
                    if (report != null && reportSalesCut != null) {
                        val file = exportToExcel(report, reportSalesCut) // devuelve el archivo generado
                        shareFile(this, file!!)
                    } else {
                        Toast.makeText(this, "No hay datos para compartir", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }


        binding.btnReturnReportCashCut.setOnClickListener {
            onBackPressed()
        }

    }

    private fun dateSelected() {
        val myCalendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener{ _, year, month, day ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, day)
            val myFormat = "dd/MM/yyyy" // Define the date format
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            binding.etReportDateCashCut.setText(sdf.format(myCalendar.time))
        }

        binding.etReportDateCashCut.setOnClickListener {
            DatePickerDialog(
                this,
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

    }

    private fun spinnerTurno() {
        // Lista de opciones
        val userTypes = listOf("Seleccione un Turno", "Mañana", "Tarde")

        // Adaptador
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            userTypes
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTurno.adapter = adapter


        binding.spinnerTurno.setSelection(0)

        // Listener
        binding.spinnerTurno.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selected = userTypes[position]
                if (position == 0) {
                    // Opción por defecto, no hacer nada
                } else {
                    turnoSelected = selected
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun visibleComponent() {
        binding.tvTitleSucCashCut.visibility = View.VISIBLE
        binding.tvSubTitleSucCashCut.visibility = View.VISIBLE
        binding.lyTitlePaymentCashCut.visibility = View.VISIBLE
        binding.lyTCreditCashCut.visibility = View.VISIBLE
        binding.lyTDebitCashCut.visibility = View.VISIBLE
        binding.lyTransfCashCut.visibility = View.VISIBLE
        binding.lyEfectiveCashCut.visibility = View.VISIBLE
        binding.dvPayment.visibility = View.VISIBLE
        binding.lyTotalCashCut.visibility = View.VISIBLE
        binding.btnExportar.visibility = View.VISIBLE
    }

    private fun validationForm(): Boolean {
        val etEmpty = "El campo no puede ser vacio"
        if (binding.etReportDateCashCut.text.toString().isEmpty()){
            binding.etReportDateCashCut.error = etEmpty
            return false
        }else if (turnoSelected == "Seleccione un Turno"){//DESPUES DE ESTRA VA EL DE LA IMAGEN
            binding.tvErrorSp.setText("Selecciona un Turno")
            binding.tvErrorSp.visibility = View.VISIBLE
            return false
        }

        binding.etReportDateCashCut.error = null
        binding.tvErrorSp.visibility = View.GONE

        return true

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun exportToExcel(report: CashCutModel, salesList: List<SalesModel>): Uri? {
        val wb = XSSFWorkbook()
        val sheet = wb.createSheet("Corte de Caja")

        // ===== Data Totales =====
        val credito  = report.ttCredito ?: 0.0
        val debito   = report.ttDebito ?: 0.0
        val transfer = report.ttTransfer ?: 0.0
        val efectivo = report.ttEfective ?: 0.0
        val total    = credito + debito + transfer + efectivo

        val fecha = binding.etReportDateCashCut.text.toString()
        val turno = turnoSelected

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

        val titleFont = wb.createFont().apply { bold = true; fontHeightInPoints = 16; color = IndexedColors.WHITE.index }
        val titleStyle = wb.createCellStyle().apply { setFillForegroundColor(IndexedColors.DARK_BLUE.index); fillPattern = FillPatternType.SOLID_FOREGROUND; alignment = HorizontalAlignment.CENTER; setFont(titleFont) }

        val metaLabelStyle = wb.createCellStyle().apply { val f = wb.createFont().apply { bold = true }; setFont(f); alignment = HorizontalAlignment.RIGHT }
        val metaValueStyle = wb.createCellStyle().apply { alignment = HorizontalAlignment.LEFT }

        val headerFont = wb.createFont().apply { bold = true; color = IndexedColors.WHITE.index }
        val headerStyle = bordered().apply { setFillForegroundColor(IndexedColors.BLUE.index); fillPattern = FillPatternType.SOLID_FOREGROUND; alignment = HorizontalAlignment.CENTER; setFont(headerFont) }
        val moneyStyle = bordered().apply { alignment = HorizontalAlignment.RIGHT; dataFormat = moneyFmt }
        val totalStyle = bordered().apply { setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index); fillPattern = FillPatternType.SOLID_FOREGROUND; alignment = HorizontalAlignment.RIGHT; val f = wb.createFont().apply { bold = true }; setFont(f); dataFormat = moneyFmt }

        val salesHeaderStyle = bordered().apply {
            setFillForegroundColor(IndexedColors.DARK_TEAL.index)
            fillPattern = FillPatternType.SOLID_FOREGROUND
            alignment = HorizontalAlignment.CENTER
            val f = wb.createFont().apply { bold = true; color = IndexedColors.WHITE.index }
            setFont(f)
        }

        // ===== Layout Totales =====
        sheet.addMergedRegion(CellRangeAddress(0,0,0,4))
        val titleRow = sheet.createRow(0)
        titleRow.heightInPoints = 28f
        titleRow.createCell(0).apply { setCellValue("REPORTE DE CORTE DE CAJA"); cellStyle = titleStyle }

        val metaRow = sheet.createRow(1)
        metaRow.createCell(0).apply { setCellValue("Fecha:"); cellStyle = metaLabelStyle }
        metaRow.createCell(1).apply { setCellValue(fecha); cellStyle = metaValueStyle }
        metaRow.createCell(3).apply { setCellValue("Turno:"); cellStyle = metaLabelStyle }
        metaRow.createCell(4).apply { setCellValue(turno); cellStyle = metaValueStyle }

        val headerRowIdx = 3
        val headerRow = sheet.createRow(headerRowIdx)
        val headers = listOf("Crédito", "Débito", "Transferencia", "Efectivo", "TOTAL")
        headers.forEachIndexed { i,h -> headerRow.createCell(i).apply { setCellValue(h); cellStyle = headerStyle as XSSFCellStyle? } }

        val dataRow = sheet.createRow(headerRowIdx + 1)
        dataRow.createCell(0).apply { setCellValue(credito); cellStyle = moneyStyle as XSSFCellStyle? }
        dataRow.createCell(1).apply { setCellValue(debito); cellStyle = moneyStyle as XSSFCellStyle? }
        dataRow.createCell(2).apply { setCellValue(transfer); cellStyle = moneyStyle as XSSFCellStyle? }
        dataRow.createCell(3).apply { setCellValue(efectivo); cellStyle = moneyStyle as XSSFCellStyle? }
        dataRow.createCell(4).apply { setCellValue(total); cellStyle = totalStyle as XSSFCellStyle? }

        // ===== Ventas individuales =====
        var salesStartRow = headerRowIdx + 4
        if(salesList.isNotEmpty()){
            // Encabezado ventas
            val salesHeader = sheet.createRow(salesStartRow++)
            val salesCols = listOf("No. Venta", "Subtotal", "Total", "Estado", "Método de Pago", "Fecha")
            salesCols.forEachIndexed { i,h -> salesHeader.createCell(i).apply { setCellValue(h); cellStyle =
                salesHeaderStyle as XSSFCellStyle?
            } }

            // Filas ventas
            salesList.forEach { s ->
                val r = sheet.createRow(salesStartRow++)
                r.createCell(0).setCellValue(s.id.toDouble())
                r.createCell(1).setCellValue(s.subTotal)
                r.createCell(2).setCellValue(s.total)
                r.createCell(3).setCellValue(s.statusSales)
                r.createCell(4).setCellValue(s.paymentMethod)
                r.createCell(5).setCellValue(s.createDate)
            }
        }

        // ===== Guardar a Descargas =====
        val fileName = "CorteCaja_${System.currentTimeMillis()}.xlsx"
        return try {
            val bos = ByteArrayOutputStream()
            wb.write(bos)
            wb.close()

            val bytes = bos.toByteArray()
            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(
                    MediaStore.Downloads.MIME_TYPE,
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                )
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

    fun showExportDialog(
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

    fun shareFile(context: Context, uri: Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/vnd.ms-excel"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Compartir Excel con:"))
    }


}