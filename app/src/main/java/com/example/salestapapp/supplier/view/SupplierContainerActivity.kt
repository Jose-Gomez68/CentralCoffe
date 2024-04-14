package com.example.salestapapp.supplier.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.salestapapp.R
import com.example.salestapapp.databinding.ActivitySupplierContainerBinding

class SupplierContainerActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySupplierContainerBinding
    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupplierContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init fragment
        val fragment = SupplierFragment()
        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.supplierContainerFragment, fragment)
        fragmentTransaction.commit()

        //floating button add supplier
        binding.fbAddSupplier.setOnClickListener {
            val newSupplier = NewSupplierFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.supplierContainerFragment, newSupplier)
                .addToBackStack(null)
                .commit()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val currentFragment = supportFragmentManager.findFragmentById(R.id.supplierContainerFragment)
        if (currentFragment is SupplierFragment){
            binding.fbAddSupplier.visibility = View.VISIBLE
        }else if (currentFragment is NewSupplierFragment){
            binding.fbAddSupplier.visibility = View.GONE
        }
    }

}