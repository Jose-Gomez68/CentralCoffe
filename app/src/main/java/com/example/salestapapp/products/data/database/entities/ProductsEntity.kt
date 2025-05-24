package com.example.salestapapp.products.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.salestapapp.products.data.model.ProductModel

@Entity(tableName = "Products")
data class ProductsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    val id: Int = 0,
    @ColumnInfo(name = "Name")
    val name: String,
    @ColumnInfo(name = "Quantity")
    val quantity: Int,
    @ColumnInfo(name = "Price")
    val price: Double,
    @ColumnInfo(name = "Image")
    val image: String,
    @ColumnInfo(name = "CategoryID")
    val categoryID: Int,
    @ColumnInfo(name = "Category")
    val category: String,
    @ColumnInfo(name = "SupplierID")
    val supplierID: Int,
    @ColumnInfo(name = "Supplier")
    val supplier: String,
    @ColumnInfo(name = "CreateDate")
    val createDate: String,
)


fun ProductModel.toDatabase() = ProductsEntity(
    id = id,
    name = name,
    quantity = quantity,
    price = price,
    image = image,
    categoryID = categoryID,
    category = category,
    supplierID = supplierID,
    supplier = supplier,
    createDate = createDate
)

/*
* @Entity(tableName = "Products")
data class ProductsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    val id: Int = 0,
    @ColumnInfo(name = "Name")
    val name: String,
    @ColumnInfo(name = "Quantity")
    val quantity: Float,
    @ColumnInfo(name = "Price")
    val price: Double,
    @ColumnInfo(name = "Image")
    val image: String,
    @ColumnInfo(name = "CategoryID")
    val categoryID: Int,
    @ColumnInfo(name = "Category")
    val category: String,
    @ColumnInfo(name = "SupplierID")
    val supplierID: Int,
    @ColumnInfo(name = "Supplier")
    val supplier: String,
    @ColumnInfo(name = "MeasurementID")
    val measurementID: Int,
    @ColumnInfo(name = "Measurement")
    val measurement: String,
    @ColumnInfo(name = "CreateDate")
    val createDate: String,
)


fun ProductModel.toDatabase() = ProductsEntity(
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
)*/