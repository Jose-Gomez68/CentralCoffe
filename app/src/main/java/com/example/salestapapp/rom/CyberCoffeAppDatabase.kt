package com.example.salestapapp.rom

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.salestapapp.login.data.database.entities.UsersEntity
import com.example.salestapapp.util.UtilsFunctions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CyberCoffeAppDatabase: Application() {

    object CyberCoffeAppDatabase {
        @Volatile
        private var instance: CyberCoffeDatabase? = null
        private var util: UtilsFunctions? = null

        fun getInstance(context: Context): CyberCoffeDatabase {
            synchronized(this) {
                if (instance == null) {
                    util = UtilsFunctions()
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CyberCoffeDatabase::class.java,
                        "cyberCoffeDatabase"
                    )
                        .addCallback(object : RoomDatabase.Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)

                                // Ejecuta en hilo aparte para no bloquear la UI
                                CoroutineScope(Dispatchers.IO).launch {
                                    val userDao = getInstance(context).usersDao()
                                    userDao.addUser(
                                        UsersEntity(
                                            id = 1,
                                            name = "Jose",
                                            lastName = "Gomez",
                                            userType = "Admin",
                                            userName = "admin",
                                            password = "1234",
                                            phone = "1234567899",
                                            createDate = util!!.getCurrentFormattedDate(),
                                            updateDate = util!!.getCurrentFormattedDate()
                                        )
                                    )
                                }
                            }
                        })
                        .build()
                }
                return instance!!
            }
        }
    }


}