package com.example.salestapapp.reports.data.model

data class ProductosMasVendidosModel(
    val productId: Int,
    val productName: String,
    val category: String,
    val totalVendidos: Int,
    val totalIngresos: Double
)
