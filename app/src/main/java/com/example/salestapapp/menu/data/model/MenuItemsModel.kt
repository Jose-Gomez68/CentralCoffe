package com.example.salestapapp.menu.data.model

data class MenuItemsModel(
    val id: Int,
    val name: String,
    val icon: Int,
    val activity: Class<*>
)
