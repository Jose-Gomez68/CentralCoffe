package com.example.salestapapp.products.data.domain

import com.example.salestapapp.products.data.ProductsRepository
import com.example.salestapapp.products.data.model.ProductModel
import com.example.salestapapp.products.data.model.toDomain

class GetProductsUseCase(private val repository: ProductsRepository) {

    suspend operator fun invoke(): List<ProductModel> {
        val products = repository.getAllData().map { it.toDomain() }

        return if (products.isNotEmpty()){
            products
        }else{
            emptyList<ProductModel>()
        }

    }

}