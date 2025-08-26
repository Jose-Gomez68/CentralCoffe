package com.example.salestapapp.sales.data.domain

import com.example.salestapapp.products.data.ProductsRepository
import com.example.salestapapp.products.data.model.ProductModel
import com.example.salestapapp.products.data.model.toDomain

class UpdateStockProductUseCase(
    private val repository: ProductsRepository
) {

    suspend operator fun invoke(productId: Int, quantity: Int): ProductModel {
        val rowsUpdated = repository.editProductStock(productId, quantity)
        return if (rowsUpdated > 0) {
            repository.getProductById(productId).toDomain()
                ?: throw Exception("Failed to retrieve updated product from database")
        } else {
            throw Exception("Failed to update product into database")
        }
    }

}