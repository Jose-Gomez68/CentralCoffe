package com.example.salestapapp.products.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.salestapapp.R
import com.example.salestapapp.databinding.ActivityProductsContainerBinding

class ProductsContainerActivity : AppCompatActivity(), OnFragmentChangedListener {

    private lateinit var binding: ActivityProductsContainerBinding

    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.fbAddProduct.visibility = View.VISIBLE

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



    }
    override fun onBackPressed() {
        super.onBackPressed()
        val currentFragment = supportFragmentManager.findFragmentById(R.id.productsContainerFragment)
        if (currentFragment is ProductsFragment) {
            binding.fbAddProduct.visibility = View.VISIBLE
        } else if (currentFragment is NewProductFragment) {
            binding.fbAddProduct.visibility = View.GONE
        }
    }

    override fun onFragmentChanged(fragment: Fragment) {
        if (fragment is ProductsFragment) {
            // Estás en ProductsFragment
            // Muestra el botón flotante
            Log.e("VISIBLE","")
            binding.fbAddProduct.visibility = View.VISIBLE
        } else if (fragment is NewProductFragment) {
            // Estás en NewProductFragment
            // Oculta el botón flotante
            Log.e("NO VISIBLE","")
            binding.fbAddProduct.visibility = View.GONE
        }  else if (fragment is EditProductFragment) {
            // Estás en NewProductFragment
            // Oculta el botón flotante
            Log.e("NO VISIBLE","")
            binding.fbAddProduct.visibility = View.GONE
        }
    }


}