package com.example.salestapapp.user.data.domain.usecase

import android.database.sqlite.SQLiteException
import android.util.Log
import com.example.salestapapp.login.data.UserRepository
import com.example.salestapapp.login.data.model.UsersModel
import com.example.salestapapp.login.data.model.toDomain

class GetUserByIDUseCase(
    private val repository: UserRepository
) {

    suspend operator fun invoke(userID: Int): UsersModel {
        return try {
            val result = repository.getUsersbyId(userID)
            result.toDomain()
        }catch (e: SQLiteException) {
            Log.e("GetUserByIdUseCase", "Error de base de datos: ${e.message}", e)
            e.printStackTrace()
            UsersModel(
                0,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
            )
        }catch (e: Exception){
            Log.e("GetUserByIdUseCase", "Error inesperado: ${e.message}", e)
            e.printStackTrace()
            UsersModel(
                0,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
            )
        }
    }

}