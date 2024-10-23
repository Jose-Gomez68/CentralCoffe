package com.example.salestapapp.login.data

import com.example.salestapapp.login.data.database.entities.UsersEntity
import com.example.salestapapp.rom.CyberCoffeDatabase

class UserRepository(private var db: CyberCoffeDatabase) {

    suspend fun addUser(user: UsersEntity): Int {
        return db.usersDao().addUser(user).toInt()
    }

    suspend fun editUser (user: UsersEntity): Int {
        return db.usersDao().editUser(user)
    }

    suspend fun getUsersbyId(userID: Int): UsersEntity {
        return db.usersDao().getUserByID(userID)
    }

    suspend fun deleteById( userID: Int ): Boolean {
        return try {
            db.usersDao().deleteUserByID(userID)
            true
        }catch (e: Exception) {
            false
        }
    }

    suspend fun getAllUser(): List<UsersEntity> {
        return db.usersDao().getAllUsers()
    }

}