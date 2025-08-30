package com.example.salestapapp.util

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.dantsu.escposprinter.connection.tcp.TcpConnection
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UtilsFunctions {

    fun showConfirmDialog(
        context: Context,
        title: String = "¿Estás seguro?",
        message: String = "¿Deseas salir de la aplicación?",
        confirmText: String = "Salir",
        cancelText: String = "Cancelar",
        onConfirm: () -> Unit,
        onCancel: (() -> Unit)? = null
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(confirmText) { dialog, _ ->
            onConfirm()
            dialog.dismiss()
        }
        builder.setNegativeButton(cancelText) { dialog, _ ->
            onCancel?.invoke()
            dialog.dismiss()
        }
        builder.setCancelable(false)
        builder.show()
    }

    fun deleteDialog(
        context: Context,
        title: String = "¿Estás seguro de eliminar?",
        message: String = "¿Deseas eliminar el item?",
        confirmText: String = "Eliminar",
        cancelText: String = "Cancelar",
        onConfirm: () -> Unit,
        onCancel: (() -> Unit)? = null
        ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton(confirmText) { dialog, which ->
            onConfirm()
            dialog.dismiss()
        }

        builder.setNegativeButton(cancelText) { dialog, which ->
            onCancel?.invoke()
            dialog.dismiss()
            dialog.cancel()
        }
        builder.show()
    }

    fun getCurrentFormattedDate(): String {
        val dateCreate = Date()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return dateFormat.format(dateCreate)
    }

    fun formmaterDate(createdDate:String): Date? {
        return try {
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            formatter.parse(createdDate)
        } catch (e: Exception) {
            Log.e("DateParse", "Error al convertir la fecha: $createdDate", e)
            null
        }
    }

    fun generatePosTicket(
        orderId: String,
        items: List<Triple<Int, String, Double>>, // Cantidad, Nombre, Precio unitario
        total: Double
    ): String {
        val sb = StringBuilder()

        // ===== Encabezado centrado =====
        sb.appendLine("[C]*** CHARLY COFFE ***")
        sb.appendLine("[C]RFC: XXX-XXXX-XXX")
        sb.appendLine("[C]Fecha: ${java.time.LocalDateTime.now()}")
        sb.appendLine("[C]--------------------------------")

        // ===== Encabezados de tabla =====
        sb.appendLine("[L]Cant   Descripción                [R]P.Unit")
        sb.appendLine("[C]--------------------------------")

        // ===== Lista de productos =====
        items.forEach { (qty, name, price) ->
            val qtyStr = qty.toString().padEnd(5) // ancho fijo para la cantidad
            val nameStr = if (name.length > 22) name.take(22) else name.padEnd(22)
            val priceStr = "%.2f".format(price)

            sb.appendLine("[L]$qtyStr$nameStr[R]$priceStr")
        }

        // ===== Separador =====
        sb.appendLine("[C]--------------------------------")

        // ===== Total centrado =====
        sb.appendLine("[C]TOTAL: $${"%.2f".format(total)}")

        // ===== Mensaje de agradecimiento centrado =====
        sb.appendLine("[C]Gracias por su compra")

        // ===== Saltos de línea antes del corte =====
        sb.appendLine("\n\n\n\n")
        sb.appendLine("[L]\n[L]\n[L]\n[L]")

        return sb.toString()
    }

    fun printViaBluetooth(context: Context, ticket: String) {
        Thread {
            try {
                val printer = EscPosPrinter(
                    BluetoothPrintersConnections.selectFirstPaired(),
                    203,
                    48f,
                    48 //32
                )
                printer.printFormattedText(ticket)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    fun printViaTcpIp(context: Context, ticket: String, ip: String, port: Int = 9100, timeout: Int = 5000) {
        Thread {
            try {
                val conection = TcpConnection(ip, port, timeout)
                val printer = EscPosPrinter(
                    conection,
                    203,   // Resolución en DPI
                    58f,   // Ancho en mm
                    48     // Caracteres por línea
                )
                printer.printFormattedTextAndCut(ticket)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    fun manualCenter(text: String, lineWidth: Int = 38): String {
        val spaces = ((lineWidth - text.length) / 2).coerceAtLeast(0)
        return " ".repeat(spaces) + text
    }


}