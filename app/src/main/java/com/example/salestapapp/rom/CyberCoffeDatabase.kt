package com.example.salestapapp.rom

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.salestapapp.login.data.database.dao.UsersDao
import com.example.salestapapp.login.data.database.entities.UsersEntity
import com.example.salestapapp.products.data.database.dao.ProductsDao
import com.example.salestapapp.products.data.database.entities.ProductsEntity
import com.example.salestapapp.supplier.data.database.dao.SuppliersDao
import com.example.salestapapp.supplier.data.database.entities.SupplierEntity

@Database(entities = [ProductsEntity::class, SupplierEntity::class, UsersEntity::class], version = 1)//recuerda subir la version con cada tabla nueva que agreges ó columnas nuevas
abstract class CyberCoffeDatabase : RoomDatabase() {

    abstract fun productsDao(): ProductsDao

    //abstract fun getProductsDao(): ProductsDao

    abstract fun suppliersDao(): SuppliersDao

    abstract fun usersDao(): UsersDao

}