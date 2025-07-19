package com.example.salestapapp.sales.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.salestapapp.sales.data.model.SalesDetailsModel

@Entity(tableName = "SalesDetail")
data class SalesDetailEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    val id: Int = 0,
    @ColumnInfo(name = "SaleID")
    val saleId: Int, // Referencia a la venta

    @ColumnInfo(name = "ProductID")
    val productId: Int, // Referencia al producto vendido

    @ColumnInfo(name = "ProductName")
    val productName: String, // Opcional, por si quieres mantener una copia del nombre

    @ColumnInfo(name = "Quantity")
    val quantity: Int, //cantidad del producto

    @ColumnInfo(name = "UnitPrice")
    val unitPrice: Double, // Precio unitario del producto al momento de la venta

    @ColumnInfo(name = "TotalPrice")
    val totalPrice: Double // unitPrice * quantity
)

fun SalesDetailsModel.toDatabase() = SalesDetailEntity(
    id = id,
    saleId = saleId,
    productId = productId,
    productName = productName,
    quantity = quantity,
    unitPrice = unitPrice,
    totalPrice = totalPrice
)
