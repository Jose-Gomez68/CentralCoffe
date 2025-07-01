package com.example.salestapapp.login.data.model

import com.example.salestapapp.login.data.database.entities.UsersEntity
import com.google.gson.annotations.SerializedName

data class UsersModel(
    @SerializedName("ID")
    val id: Int = 0,
    @SerializedName("Name")
    val name: String,
    @SerializedName("LastName")
    val lastName: String,
    @SerializedName("UserName")
    val userName: String,
    @SerializedName("Phone")
    val phone: String,
    @SerializedName("CreateDate")
    val createDate: String,
)

fun UsersEntity.toDomain() = UsersModel(
    id = id,
    name = name,
    lastName = lastName,
    userName = userName,
    phone = phone,
    createDate = createDate
)
