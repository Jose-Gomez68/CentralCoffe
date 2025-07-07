package com.example.salestapapp.user.data.domain.usecase

import com.example.salestapapp.login.data.UserRepository
import com.example.salestapapp.login.data.database.entities.toDatabase
import com.example.salestapapp.login.data.model.UsersModel
import com.example.salestapapp.login.data.model.toDomain

class SaveUserUseCase(
    private val repository: UserRepository
) {

    suspend fun invoke(user: UsersModel): UsersModel {
        val userID = repository.addUser(user.toDatabase())
        val getNewUser = repository.getUsersbyId(userID)

        if(getNewUser != null){
            return getNewUser.toDomain()
        } else {
            throw Exception("Faild to insert user into database")
        }
    }

}