package com.example.salestapapp.category.ui.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.salestapapp.R
import com.example.salestapapp.category.ui.CategoryListAdapter
import com.example.salestapapp.databinding.FragmentCategoryBinding
import com.example.salestapapp.products.ui.ProductListAdapter
import com.example.salestapapp.products.ui.view.OnFragmentChangedListener
import com.example.salestapapp.products.ui.viewmodel.ProductsListViewModel
import com.example.salestapapp.rom.CyberCoffeAppDatabase
import com.example.salestapapp.rom.CyberCoffeDatabase
import com.example.salestapapp.util.UtilsFunctions


class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoryAdap: CategoryListAdapter
    private lateinit var db: CyberCoffeDatabase
//    private lateinit var viewModel: ProductsListViewModel
    private var listener: OnCategoryFragmentChangeListener? = null
    private lateinit var util: UtilsFunctions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       db = CyberCoffeAppDatabase.CyberCoffeAppDatabase.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        util = UtilsFunctions()
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCategoryFragmentChangeListener) {
            listener = context
            listener?.onCategoryFragmentChangeListener(this)
        } else {
            throw RuntimeException("$context must implement OnCategoryFragmentChangeListener")
        }
    }
}