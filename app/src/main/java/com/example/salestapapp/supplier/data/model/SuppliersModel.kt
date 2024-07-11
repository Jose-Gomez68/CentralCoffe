package com.example.salestapapp.supplier.data.model

import com.example.salestapapp.supplier.data.database.entities.SupplierEntity
import com.google.gson.annotations.SerializedName

data class SuppliersModel (
    @SerializedName("ID")
    val id: Int = 0,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Phone")
    val phone: String,
    @SerializedName("Address")
    val address: String,
    @SerializedName("CreateDate")
    val createDate: String,
)

fun SupplierEntity.toDomain() = SuppliersModel(
    id = id,
    name = name,
    phone = phone,
    address = address,
    createDate = createDate
)