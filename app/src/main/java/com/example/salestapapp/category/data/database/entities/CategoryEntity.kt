package com.example.salestapapp.category.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.salestapapp.category.data.model.CategoryModel

@Entity(tableName = "Category")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    val id: Int = 0,
    @ColumnInfo(name = "Name")
    val name: String,
    @ColumnInfo(name = "CreateDate")
    val createDate: String,
    @ColumnInfo(name = "UpdateDate")
    val updateDate: String

)


fun CategoryModel.toDatabase() = CategoryEntity(
    id = id,
    name = name,
    createDate = createDate,
    updateDate = updateDate
)