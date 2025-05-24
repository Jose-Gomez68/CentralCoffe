package com.example.salestapapp.util

import android.app.AlertDialog
import android.content.Context

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


}