package com.example.salestapapp.sales.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.salestapapp.sales.data.model.SalesModel

@Entity(tableName = "Sales")
data class SalesEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    val id: Int = 0,
    @ColumnInfo(name = "SubTotal")
    val subTotal: Double,
    @ColumnInfo(name = "Total")
    val total: Double,
    @ColumnInfo(name = "StatusSales")
    val statusSales: String, // Ejemplo: "COMPLETADA", "CANCELADA", "PENDIENTE"
    @ColumnInfo(name = "PaymentMethod")
    val paymentMethod: String, // Ejemplo: "EFECTIVO", "TARJETA", "TRANSFERENCIA"
    @ColumnInfo(name = "CreateDate")
    val createDate: String

)

fun SalesModel.toDatabase() = SalesEntity(
    id = id,
    subTotal = subTotal,
    total = total,
    statusSales = statusSales,
    paymentMethod = paymentMethod,
    createDate = createDate
)