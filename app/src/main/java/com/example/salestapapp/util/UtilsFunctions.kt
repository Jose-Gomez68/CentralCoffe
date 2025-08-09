package com.example.salestapapp.util

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection
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
        items: List<Pair<String, Double>>,
        total: Double
    ): String {
        val sb = StringBuilder()
        sb.appendLine("[C]<b>*** TIENDA XYZ ***</b>")
        sb.appendLine("[C]RFC: XXX-XXXX-XXX")
        sb.appendLine("[C]Fecha: ${java.time.LocalDateTime.now()}")
        sb.appendLine("[C]-------------------------------")
        items.forEach { (name, price) ->
            sb.appendLine("[L]$name[R]$${"%.2f".format(price)}")
        }
        sb.appendLine("[C]-------------------------------")
        sb.appendLine("[R]<b>TOTAL: $${"%.2f".format(total)}</b>")
        sb.appendLine("[C]Gracias por su compra")
        sb.appendLine("\n\n\n") // Espacios para corte
        return sb.toString()
    }


    fun printViaBluetooth(context: Context, ticket: String) {
        Thread {
            try {
                val printer = EscPosPrinter(
                    BluetoothPrintersConnections.selectFirstPaired(),
                    203,
                    48f,
                    32
                )
                printer.printFormattedText(ticket)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    fun printViaTcpIp(context: Context, ticket: String, ip: String, port: Int = 9100, timeout: Int = 5) {
        Thread {
            try {
                val printer = EscPosPrinter(
                    TcpConnection(ip, port, timeout),
                    203,   // Resolución en DPI
                    48f,   // Ancho en mm
                    32     // Caracteres por línea
                )
                printer.printFormattedText(ticket)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }


}