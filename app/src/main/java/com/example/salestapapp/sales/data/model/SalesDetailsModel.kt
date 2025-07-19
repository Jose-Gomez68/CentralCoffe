package com.example.salestapapp.sales.data.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.example.salestapapp.sales.data.database.entities.SalesDetailEntity
import com.google.gson.annotations.SerializedName

data class SalesDetailsModel(
    @SerializedName("ID")
    val id: Int = 0,
    @SerializedName("SaleID")
    val saleId: Int, // Referencia a la venta

    @SerializedName("ProductID")
    val productId: Int, // Referencia al producto vendido

    @SerializedName("ProductName")
    val productName: String, // Opcional, por si quieres mantener una copia del nombre

    @SerializedName("Quantity")
    val quantity: Int, //cantidad del producto

    @SerializedName("UnitPrice")
    val unitPrice: Double, // Precio unitario del producto al momento de la venta

    @SerializedName("TotalPrice")
    val totalPrice: Double // unitPrice * quantity
)

fun SalesDetailEntity.toDomain() = SalesDetailsModel(
    id = id,
    saleId = saleId,
    productId = productId,
    productName = productName,
    quantity = quantity,
    unitPrice = unitPrice,
    totalPrice = totalPrice
)
