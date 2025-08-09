package com.example.salestapapp.sales.data

import androidx.room.withTransaction
import com.example.salestapapp.rom.CyberCoffeDatabase
import com.example.salestapapp.sales.data.database.entities.SalesDetailEntity
import com.example.salestapapp.sales.data.database.entities.SalesEntity

class SalesRepository(private var db: CyberCoffeDatabase) {

    suspend fun insertSales (sale: SalesEntity): Int{
        return db.salesDao().insertOne(sale).toInt()
    }

    suspend fun editSale (sale: SalesEntity): Int{
        return db.salesDao().editSales(sale)
    }

    suspend fun getSaleById(saleID: Int): SalesEntity {
        return db.salesDao().getSalesByID(saleID)
    }

    suspend fun deleteById (saleID: Int):Boolean {
        return try {
            db.salesDao().deleteSalesById(saleID)
            true
        }catch(e: Exception){
            false
        }

    }

    suspend fun getAllSales (): List<SalesEntity> {
        return db.salesDao().getAllSales()
    }

    /*DETAILS QUERYS*/
    suspend fun insertSalesDetails (saleDetail: SalesDetailEntity): Int{
        return db.salesDetailDao().insertOne(saleDetail).toInt()
    }

    suspend fun insertSalesDetailsList (saleDetailList: List<SalesDetailEntity>): Int{
        val insertedIds = db.salesDetailDao().insertAllDetails(saleDetailList)
        return insertedIds.size // devuelve el número de registros insertados
    }

    suspend fun editSalesDetails (salesDetail: SalesDetailEntity): Int{
        return db.salesDetailDao().editSalesDetails(salesDetail)
    }

    suspend fun getSalesDetailsListById(saleID: Int): List<SalesDetailEntity> {
        return db.salesDetailDao().getSalesDetailsByID(saleID)
    }

    suspend fun deleteSalesDetailsById (saleID: Int):Boolean {
        return try {
            db.salesDetailDao().deleteSalesDetailsById(saleID)
            true
        }catch(e: Exception){
            false
        }

    }

    suspend fun getAllSalesDetails(): List<SalesDetailEntity> {
        return db.salesDetailDao().getAllSalesDetails()
    }

    suspend fun insertSaleAndDetailsTransaction(
        sale: SalesEntity,
        details: List<SalesDetailEntity>
    ): Int?  {
        return try {
            var saleIdResult: Int? = null
            db.withTransaction {
                val saleId = db.salesDao().insertOne(sale)
                if (saleId <= 0) throw Exception("Error al insertar venta")

                val detailsWithSaleId = details.map {
                    SalesDetailEntity(
                        id = it.id, // normalmente es 0 porque es autogenerado
                        saleId = saleId.toInt(), // Asignar el ID de la venta insertada
                        productId = it.productId,
                        productName = it.productName,
                        quantity = it.quantity,
                        unitPrice = it.unitPrice,
                        totalPrice = it.totalPrice
                    )
                }

                val insertedIds = db.salesDetailDao().insertAllDetails(detailsWithSaleId)
                if (insertedIds.size != details.size) {
                    throw Exception("Error al insertar algunos detalles")
                }
                saleIdResult = saleId.toInt()
            }
            saleIdResult // todo correcto
        } catch (e: Exception) {
            e.printStackTrace()
            null // hubo rollback
        }
    }


}