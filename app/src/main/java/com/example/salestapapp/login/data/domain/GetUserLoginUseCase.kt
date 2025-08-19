package com.example.salestapapp.login.data.domain

import android.database.sqlite.SQLiteException
import android.util.Log
import com.example.salestapapp.login.data.UserRepository
import com.example.salestapapp.login.data.model.UsersModel
import com.example.salestapapp.login.data.model.toDomain

class GetUserLoginUseCase(
    private val repository: UserRepository
) {

    suspend operator fun invoke(userName: String, password: String): UsersModel {
        return try {
            val result = repository.getUsersLogin(userName, password)
            result?.toDomain() ?: UsersModel(0, "", "", "", "", "", "", "", "")
        } catch (e: SQLiteException) {
            Log.e("GetUserLoginUseCase", "Error de base de datos: ${e.message}", e)
            UsersModel(0, "", "", "", "", "","", "", "")
        } catch (e: Exception) {
            Log.e("GetUserLoginUseCase", "Error inesperado: ${e.message}", e)
            UsersModel(0, "", "", "","", "", "", "", "")
        }
    }

}