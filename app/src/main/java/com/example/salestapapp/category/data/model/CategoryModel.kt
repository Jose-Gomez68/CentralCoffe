package com.example.salestapapp.category.data.model

import com.google.gson.annotations.SerializedName

data class CategoryModel(
    @SerializedName("ID")
    val id: Int,
    @SerializedName("Name")
    val name: String
)
