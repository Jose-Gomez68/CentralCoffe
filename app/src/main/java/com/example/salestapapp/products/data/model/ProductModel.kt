package com.example.salestapapp.products.data.model

import androidx.room.ColumnInfo
import com.example.salestapapp.products.data.database.entities.ProductsEntity
import com.google.gson.annotations.SerializedName

data class ProductModel(
    @SerializedName("ID")
    val id: Int,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Quantity")
    val quantity: Float,
    @SerializedName("Price")
    val price: Double,
    @SerializedName("Image")
    val image: String,
    @SerializedName("CategoryID")
    val categoryID: Int,
    @SerializedName("Category")
    val category: String,
    @SerializedName("SupplierID")
    val supplierID: Int,
    @SerializedName("Supplier")
    val supplier: String,
    @SerializedName("MeasurementID")
    val measurementID: Int,
    @SerializedName("Measurement")
    val measurement: String,
    @SerializedName("CreateDate")
    val createDate: String,
)

fun ProductsEntity.toDomain() = ProductModel(
    id = id,
    name = name,
    quantity = quantity,
    price = price,
    image = image,
    categoryID = categoryID,
    category = category,
    supplierID = supplierID,
    supplier = supplier,
    measurementID = measurementID,
    measurement = measurement,
    createDate = createDate
)