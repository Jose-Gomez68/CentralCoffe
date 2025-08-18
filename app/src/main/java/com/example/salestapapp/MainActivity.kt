package com.example.salestapapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.salestapapp.login.view.ui.LoginActivity
import com.example.salestapapp.menu.ui.view.MenuActivity
import com.example.salestapapp.util.SharedPreferencesUtil

class MainActivity : AppCompatActivity() {

    private var sharedPref: SharedPreferencesUtil? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPref = SharedPreferencesUtil(applicationContext)
        //TODO inciar la db osea has una simple consulta para que la db se inicie al principio
        //y ek app inspector la pueda leer correctamente
        //esta debe mandar a la view de MenuActivity LoginActivity
        if (sharedPref?.getName().isNullOrEmpty()){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }else {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}