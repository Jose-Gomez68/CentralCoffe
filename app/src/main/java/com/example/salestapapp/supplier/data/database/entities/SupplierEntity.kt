package com.example.salestapapp.supplier.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
)
