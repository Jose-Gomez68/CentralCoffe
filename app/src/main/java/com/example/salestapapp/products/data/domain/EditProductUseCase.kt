package com.example.salestapapp.products.data.domain

import com.example.salestapapp.products.data.ProductsRepository
import com.example.salestapapp.products.data.database.entities.toDatabase
import com.example.salestapapp.products.data.model.ProductModel
import com.example.salestapapp.products.data.model.toDomain

class EditProductUseCase(private val repository: ProductsRepository) {

    suspend operator fun invoke(product: ProductModel): ProductModel {
        //cuando sean listado lo que valla insertar seria algo asi repository.insertProduct(product.map {it.toDatabase() } )
        // Recuperar el producto recién insertado de la base de datos
        val editProduct = repository.editProduct(product.toDatabase())
        val getEditProduct = repository.getProductById(editProduct)

        // Verificar si el producto fue insertado correctamente
        if (getEditProduct != null) {
            // Convertir el producto de la base de datos de nuevo al modelo de la aplicación
            return getEditProduct.toDomain()
        } else {
            // Manejar el caso en el que no se pudo insertar el producto correctamente
            throw Exception("Failed to update product into database")
        }
    }

}