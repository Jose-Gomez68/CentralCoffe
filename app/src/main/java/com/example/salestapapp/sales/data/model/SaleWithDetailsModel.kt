package com.example.salestapapp.sales.data.model

import com.google.gson.annotations.SerializedName

data class SaleWithDetailsModel(
    @SerializedName("Sales")
    val sales: SalesModel,
    @SerializedName("SalesDetails")
    val salesDetails: List<SalesDetailsModel>
)
