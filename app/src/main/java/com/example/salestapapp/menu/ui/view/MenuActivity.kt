package com.example.salestapapp.menu.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.salestapapp.R
import com.example.salestapapp.category.ui.view.CategoryContainerActivity
import com.example.salestapapp.databinding.ActivityMainBinding
import com.example.salestapapp.menu.data.model.MenuItemsModel
import com.example.salestapapp.menu.data.model.adapter.MenuItemsAdapter
import com.example.salestapapp.products.ui.view.ProductsContainerActivity
import com.example.salestapapp.supplier.view.SupplierContainerActivity

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var menuAdapter: MenuItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //add modules buttons to list// id, Name module, Icon Module, Activity Module
        val menu = listOf(
            MenuItemsModel(1,"Trabajadores", R.drawable.worker,  ProductsContainerActivity::class.java),
            MenuItemsModel(2,"Productos", R.drawable.products,  ProductsContainerActivity::class.java),
            MenuItemsModel(3,"Proovedores", R.drawable.suppliers,  SupplierContainerActivity::class.java),
            MenuItemsModel(4,"Ventas", R.drawable.gallery,  SupplierContainerActivity::class.java),
            MenuItemsModel(5,"Categorias", R.drawable.gallery,  CategoryContainerActivity::class.java)
        )

        menuAdapter = MenuItemsAdapter(menu)

        binding.rvMainActivity.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = menuAdapter
        }

    }
}