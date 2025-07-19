package com.example.salestapapp.sales.data.model

import androidx.room.ColumnInfo
import com.example.salestapapp.sales.data.database.entities.SalesEntity
import com.google.gson.annotations.SerializedName

data class SalesModel(
    @SerializedName("ID")
    val id: Int = 0,
    @SerializedName("SubTotal")
    val subTotal: Double,
    @SerializedName("Total")
    val total: Double,
    @SerializedName("StatusSales")
    val statusSales: String, // Ejemplo: "COMPLETADA", "CANCELADA", "PENDIENTE"
    @SerializedName("PaymentMethod")
    val paymentMethod: String, // Ejemplo: "EFECTIVO", "TARJETA", "TRANSFERENCIA"
    @SerializedName("CreateDate")
    val createDate: String
)

fun SalesEntity.toDomain() = SalesModel(
    id = id,
    subTotal = subTotal,
    total = total,
    statusSales = statusSales,
    paymentMethod = paymentMethod,
    createDate = createDate
)
