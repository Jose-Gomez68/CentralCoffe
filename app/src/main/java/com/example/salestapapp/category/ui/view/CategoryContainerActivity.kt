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
//        val fragment =

        binding.fbAddCategory.setOnClickListener {

        }
        /*enableEdgeToEdge()
        setContentView(R.layout.activity_category_container)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val currentFragment = supportFragmentManager.findFragmentById(R.id.productsContainerFragment)
        if (currentFragment is ProductsFragment) {
            binding.fbAddCategory.visibility = View.VISIBLE
        } else if (currentFragment is NewProductFragment) {
            binding.fbAddCategory.visibility = View.GONE
        }
    }

}