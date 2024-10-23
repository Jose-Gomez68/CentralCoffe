package com.example.salestapapp.login.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.salestapapp.login.data.database.entities.UsersEntity

@Dao
interface UsersDao {
 //falta agrergar ala db
    @Query("SELECT *FROM Users ORDER BY Name DESC")
    suspend fun  getAllUsers(): List<UsersEntity>

    @Query("SELECT *FROM Users WHERE id = :userID")
    suspend fun  getUserByID(userID: Int): UsersEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)//aqui le digo que si hay uno igual que lo remplaze
    suspend fun addUser(user: UsersEntity): Long

    @Query("DELETE FROM Users WHERE id = :userID")
    suspend fun deleteUserByID(userID: Int)

    @Update
    suspend fun editUser(user:UsersEntity): Int

}