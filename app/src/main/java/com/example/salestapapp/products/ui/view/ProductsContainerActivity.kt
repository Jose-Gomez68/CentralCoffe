package com.example.salestapapp.products.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.salestapapp.R
import com.example.salestapapp.databinding.ActivityProductsContainerBinding

class ProductsContainerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductsContainerBinding

    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val fragment = NewProductFragment()
        val fragment = ProductsFragment()
        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.productsContainerFragment, fragment)
        fragmentTransaction.commit()

        binding.fbAddProduct.setOnClickListener{
            val newProduct = NewProductFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.productsContainerFragment, newProduct)
                .addToBackStack(null) // Opcional: agrega el fragmento actual a la pila de retroceso
                .commit()
        }

        // Aquí puedes obtener el fragmento actual Y asi poder ocultar el floating button
        val currentFragment = supportFragmentManager.findFragmentById(R.id.productsContainerFragment)
        if (currentFragment is ProductsFragment) {
            // Estás en ProductsFragment
            // Realiza las operaciones específicas de ProductsFragment aquí si es necesario
            binding.fbAddProduct.visibility = View.VISIBLE
        } else if (currentFragment is NewProductFragment) {
            // Estás en NewProductFragment
            // Realiza las operaciones específicas de NewProductFragment aquí si es necesario
            binding.fbAddProduct.visibility = View.GONE
        }

    }
}