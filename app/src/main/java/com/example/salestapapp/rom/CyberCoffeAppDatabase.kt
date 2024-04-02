package com.example.salestapapp.rom

import android.app.Application
import android.content.Context
import androidx.room.Room

class CyberCoffeAppDatabase: Application() {

    object CyberCoffeAppDatabase {
        @Volatile
        private var instance: CyberCoffeDatabase? = null

        fun getInstance(context: Context): CyberCoffeDatabase {
            synchronized(this) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CyberCoffeDatabase::class.java,
                        "cyberCoffeDatabase"
                    ).build()
                }
                return instance!!
            }
        }
    }


}