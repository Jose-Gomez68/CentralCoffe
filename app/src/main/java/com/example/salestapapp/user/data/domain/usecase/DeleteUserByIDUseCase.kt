package com.example.salestapapp.user.data.domain.usecase

import com.example.salestapapp.login.data.UserRepository
import com.example.salestapapp.login.data.model.UsersModel

class DeleteUserByIDUseCase(private val repository: UserRepository) {

    suspend operator fun invoke(user: UsersModel): Boolean {
        val deleteUser = repository.deleteById(user.id)

        if (deleteUser) {
            return true
        }else {
            return false
            throw Exception("Failed to insert user into to database")
        }
    }

}