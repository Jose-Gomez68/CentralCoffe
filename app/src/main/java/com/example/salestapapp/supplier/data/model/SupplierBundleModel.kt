package com.example.salestapapp.supplier.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SupplierBundleModel (
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
    @SerializedName("ImageSupplier")
    val imageSupplier: String,
):Serializable