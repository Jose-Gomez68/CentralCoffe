package com.example.salestapapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.salestapapp.login.view.ui.LoginActivity
import com.example.salestapapp.menu.ui.view.MenuActivity
import com.example.salestapapp.products.ui.view.NewProductFragment
import com.example.salestapapp.products.ui.view.ProductsContainerActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //TODO inciar la db osea has una simple consulta para que la db se inicie al principio
        //y ek app inspector la pueda leer correctamente
        //esta debe mandar a la view de MenuActivity LoginActivity
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)

    }
}