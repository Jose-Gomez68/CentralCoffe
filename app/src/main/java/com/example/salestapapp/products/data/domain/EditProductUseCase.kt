package com.example.salestapapp.products.data.domain

import android.util.Log
import com.example.salestapapp.products.data.ProductsRepository
import com.example.salestapapp.products.data.database.entities.toDatabase
import com.example.salestapapp.products.data.model.ProductModel
import com.example.salestapapp.products.data.model.toDomain

class EditProductUseCase(private val repository: ProductsRepository) {

    suspend operator fun invoke(product: ProductModel): ProductModel {
        val rowsUpdated = repository.editProduct(product.toDatabase())
        return if (rowsUpdated > 0) {
            repository.getProductById(product.id)?.toDomain()
                ?: throw Exception("Failed to retrieve updated product from database")
        } else {
            throw Exception("Failed to update product into database")
        }
    }


}