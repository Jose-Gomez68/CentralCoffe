package com.example.salestapapp.products.data

import com.example.salestapapp.products.data.database.entities.ProductsEntity
import com.example.salestapapp.rom.CyberCoffeDatabase

class ProductsRepository(private var db: CyberCoffeDatabase) {

    //private lateinit var productDao: ProductsDao
    //private val productDao = ProductsDao()

    suspend fun insertProduct (product: ProductsEntity): Int{
        //return productDao.insertOne(product).toInt()
        return db.productsDao().insertOne(product).toInt()
    }

    suspend fun editProduct (product: ProductsEntity): Int{
        return db.productsDao().editProduct(product)
    }

    suspend fun getProductById(productID: Int): ProductsEntity{
        return db.productsDao().getProductByID(productID)
    }

    suspend fun deleteById (productID: Int):Boolean {
        return try {
            db.productsDao().deleteProductById(productID)
            true
        }catch(e: Exception){
            false
        }

    }

    suspend fun getAllData (): List<ProductsEntity> {
        return db.productsDao().getAllProducts()
    }

}