package com.example.salestapapp.category.ui.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.salestapapp.R
import com.example.salestapapp.databinding.ActivityCategoryContainerBinding
import com.example.salestapapp.products.ui.view.NewProductFragment
import com.example.salestapapp.products.ui.view.ProductsFragment

class CategoryContainerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryContainerBinding

    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.fbAddCategory.visibility = View.VISIBLE

        val fragment = CategoryFragment()
        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.categoryContainerFragment, fragment)
        fragmentTransaction.commit()

        /*binding.fbAddCategory.setOnClickListener {
            val newCategory = NewCategoryFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.categoryContainerFragment, newCategory)
                .addToBackStack(null)// Opcional: agrega el fragmento actual a la pila de retroceso
                .commit()
        }*/

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val currentFragment = supportFragmentManager.findFragmentById(R.id.categoryContainerFragment)
        if (currentFragment is ProductsFragment) {
            binding.fbAddCategory.visibility = View.VISIBLE
        } else if (currentFragment is NewProductFragment) {
            binding.fbAddCategory.visibility = View.GONE
        }
    }

}