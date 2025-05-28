package com.example.salestapapp.category.data.model

import com.example.salestapapp.category.data.database.entities.CategoryEntity
import com.google.gson.annotations.SerializedName

data class CategoryModel(
    @SerializedName("ID")
    val id: Int,
    @SerializedName("Name")
    val name: String
)


fun CategoryEntity.toDomain() = CategoryModel(
    id = id,
    name = name
)