package com.example.salestapapp.util

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import com.example.salestapapp.R
import com.example.salestapapp.products.data.model.ProductModel
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

}