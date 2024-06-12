package com.example.salestapapp.supplier.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.salestapapp.R
import com.example.salestapapp.databinding.FragmentSupplierBinding

class SupplierFragment : Fragment() {

    private lateinit var binding: FragmentSupplierBinding
    private var listener: OnSupplierFragmentChangeListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSupplierBinding.inflate(inflater, container,false)

        binding.btnReturnSupp.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSupplierFragmentChangeListener) {
            listener = context
            listener?.onSupplierFragmentChangeListener(this)
        } else {
            throw RuntimeException("$context must implement OnFragmentChangedListener")
        }
    }

}