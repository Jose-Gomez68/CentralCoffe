package com.example.salestapapp.products.data.domain

import com.example.salestapapp.products.data.ProductsRepository
import com.example.salestapapp.products.data.model.ProductModel
import com.example.salestapapp.products.data.model.toDomain

class GetProductByIdUseCase(private val repository: ProductsRepository) {

    suspend operator fun invoke(productID: Int): ProductModel {

        return repository.getProductById(productID).toDomain()
            ?: ProductModel(
                id = 0,
                name = "",
                quantity = 0f,
                price = 0.0,
                image = "",
                categoryID = 0,
                category = "",
                supplierID = 0,
                supplier = "",
                measurementID = 0,
                measurement = "",
                createDate = ""
            )

    }

}