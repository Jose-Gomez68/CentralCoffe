package com.example.salestapapp.user.ui.view

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.salestapapp.R
import com.example.salestapapp.databinding.ActivitySupplierContainerBinding
import com.example.salestapapp.databinding.ActivityUserContainerBinding
import com.example.salestapapp.supplier.ui.view.EditSupplierFragment
import com.example.salestapapp.supplier.ui.view.NewSupplierFragment
import com.example.salestapapp.supplier.ui.view.SupplierFragment

class UserContainerActivity : AppCompatActivity(), OnUserFragmentChangeListener {

    private lateinit var binding: ActivityUserContainerBinding
    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init fragment
        val fragment = UserFragment()
        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.userContainerFragment, fragment)
        fragmentTransaction.commit()

        //floating button
        binding.fbAddUser.setOnClickListener {
            val newUser = NewUserFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.userContainerFragment, newUser)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        /*val currentFragment = supportFragmentManager.findFragmentById(R.id.userContainerFragment)
        if (currentFragment is UserFragment){
            binding.fbAddUser.visibility = View.VISIBLE
        }else if (currentFragment is NewUserFragment){
            binding.fbAddUser.visibility = View.GONE
        }else if (currentFragment is EditUserFragment){
            binding.fbAddUser.visibility = View.GONE
        }*/
    }

    override fun onUserFragmentChangeListener(fragment: Fragment) {
        if (fragment is UserFragment){
            binding.fbAddUser.visibility = View.VISIBLE
        }else if (fragment is NewUserFragment){
            binding.fbAddUser.visibility = View.GONE
        }else if (fragment is EditUserFragment){
            binding.fbAddUser.visibility = View.GONE
        }
    }


}