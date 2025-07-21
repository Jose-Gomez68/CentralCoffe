package com.example.salestapapp.sales.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.salestapapp.R
import com.example.salestapapp.databinding.ActivitySaleContainterBinding

class SaleContainerActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySaleContainterBinding
    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaleContainterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment = SaleFragment()
        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.saleContainerFragment, fragment)
        fragmentTransaction.commit()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val currentFragment = supportFragmentManager.findFragmentById(R.id.saleContainerFragment)
    }

}