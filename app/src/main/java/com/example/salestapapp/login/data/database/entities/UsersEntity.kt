package com.example.salestapapp.login.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.salestapapp.login.data.model.UsersModel

@Entity(tableName = "Users")
data class UsersEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    val id: Int = 0,
    @ColumnInfo("Name")
    val name: String,
    @ColumnInfo("LastName")
    val lastName: String,
    @ColumnInfo("UserName")
    val userName: String,
    @ColumnInfo("Phone")
    val phone: String,
    @ColumnInfo("CreateDate")
    val createDate: String,
)

fun UsersModel.toDatabase() = UsersEntity(
    id = id,
    name = name,
    lastName = lastName,
    userName = userName,
    phone = phone,
    createDate = createDate
)