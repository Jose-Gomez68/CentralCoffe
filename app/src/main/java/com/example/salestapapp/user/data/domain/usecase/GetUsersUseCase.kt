package com.example.salestapapp.user.data.domain.usecase

import com.example.salestapapp.login.data.UserRepository
import com.example.salestapapp.login.data.model.UsersModel
import com.example.salestapapp.login.data.model.toDomain

class GetUsersUseCase(private val repository: UserRepository) {

    suspend operator fun invoke(): List<UsersModel> {
        val user = repository.getAllUser().map {
            it.toDomain()
        }

        return if (user.isNotEmpty()){
            user
        }else{
            emptyList<UsersModel>()
        }
    }

}