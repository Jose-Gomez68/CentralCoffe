package com.example.salestapapp.user.data.domain.usecase

import android.util.Log
import com.example.salestapapp.login.data.UserRepository
import com.example.salestapapp.login.data.database.entities.toDatabase
import com.example.salestapapp.login.data.model.UsersModel
import com.example.salestapapp.login.data.model.toDomain

class EditUserUseCase(private val repository: UserRepository) {

    suspend fun invoke(usersModel: UsersModel): UsersModel {
        return try {
            val rowsUpdateUser = repository.editUser(usersModel.toDatabase())
            return if (rowsUpdateUser > 0) {
                repository.getUsersbyId(usersModel.id)?.toDomain()
                    ?: throw Exception("Failed to retrieve updated user from database")
            } else {
                throw Exception("Failed to update user into database")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("EditSupplierUseCase", "Error update supplier", e)
            throw e
        }
    }

}