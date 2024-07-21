package com.example.salestapapp.supplier.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.salestapapp.supplier.data.model.SuppliersModel

@Entity(tableName = "Suppliers")
data class SupplierEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    val id: Int = 0,
    @ColumnInfo(name = "Name")
    val name: String,
    @ColumnInfo(name = "Phone")
    val phone: String,
    @ColumnInfo(name = "Address")
    val address: String,
    @ColumnInfo(name = "CreateDate")
    val createDate: String,
    @ColumnInfo(name = "ImageSupplier")
    val imageSupplier: String,
)

fun  SuppliersModel.toDatabase() = SupplierEntity(
    id = id,
    name = name,
    phone = phone,
    address = address,
    createDate = createDate,
    imageSupplier = imageSupplier
)
