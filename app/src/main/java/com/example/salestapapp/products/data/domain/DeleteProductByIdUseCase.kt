package com.example.salestapapp.products.data.domain

import com.example.salestapapp.products.data.ProductsRepository
import com.example.salestapapp.products.data.model.ProductModel

class DeleteProductByIdUseCase(private val repository: ProductsRepository)  {

    suspend operator fun invoke(product: ProductModel): Boolean {
        val deleteProduct = repository.deleteById(product.id)

        if (deleteProduct) {
            return true
        } else {
            return false
            throw Exception("Failed to delete product into database")
        }
    }

}