package com.example.salestapapp.menu.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.salestapapp.R
import com.example.salestapapp.category.ui.view.CategoryContainerActivity
import com.example.salestapapp.databinding.ActivityMainBinding
import com.example.salestapapp.login.view.ui.LoginActivity
import com.example.salestapapp.menu.data.model.MenuItemsModel
import com.example.salestapapp.menu.data.model.adapter.MenuItemsAdapter
import com.example.salestapapp.products.ui.view.ProductsContainerActivity
import com.example.salestapapp.reports.ui.view.ReportActivity
import com.example.salestapapp.sales.ui.view.SaleContainerActivity
import com.example.salestapapp.supplier.ui.view.SupplierContainerActivity
import com.example.salestapapp.user.ui.view.UserContainerActivity
import com.example.salestapapp.util.SharedPreferencesUtil

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var menuAdapter: MenuItemsAdapter
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPref = SharedPreferencesUtil(applicationContext)
        //add modules buttons to list// id, Name module, Icon Module, Activity Module
        val allMenu = listOf(
            MenuItemsModel(1,"Trabajadores", R.drawable.worker,  UserContainerActivity::class.java),
            MenuItemsModel(2,"Productos", R.drawable.products,  ProductsContainerActivity::class.java),
            MenuItemsModel(3,"Proovedores", R.drawable.suppliers,  SupplierContainerActivity::class.java),
            MenuItemsModel(4,"Ventas", R.drawable.gallery,  SaleContainerActivity::class.java),
            MenuItemsModel(5,"Categorias", R.drawable.gallery,  CategoryContainerActivity::class.java),
            MenuItemsModel(5,"Reportes", R.drawable.gallery,  ReportActivity::class.java)

        )

        // Filtramos según el tipo de usuario
        val menu = if (sharedPref.getUserType() == "Admin") {
            allMenu
        } else {
            allMenu.filter { it.name == "Productos" || it.name == "Ventas" }
        }


        Log.e("AQUI", sharedPref.getName().toString())

        menuAdapter = MenuItemsAdapter(menu)

        binding.rvMainActivity.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = menuAdapter
        }

        binding.btnLogout.setOnClickListener {
            sharedPref!!.clearPref()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finishAffinity() // cierra toda la app
            return
        } else {
            Toast.makeText(this, "Presiona atrás de nuevo para salir", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

}