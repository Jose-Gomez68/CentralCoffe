package com.example.salestapapp.supplier.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.salestapapp.R
import com.example.salestapapp.databinding.FragmentNewSupplierBinding
import com.example.salestapapp.supplier.data.model.SupplierBundleModel
import com.example.salestapapp.supplier.data.model.SuppliersModel

class NewSupplierFragment : Fragment() {

    private lateinit var binding: FragmentNewSupplierBinding
    private var listener: OnSupplierFragmentChangeListener? = null
    private var supplier: SuppliersModel? = null

    companion object {
        private const val ARG_SUPPLIER = "supplier"
        fun newInstance(supplier: SupplierBundleModel): NewSupplierFragment {
            val fragment = NewSupplierFragment()
            val args = Bundle()
            args.putSerializable(ARG_SUPPLIER, supplier)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supplier = arguments?.getSerializable(ARG_SUPPLIER) as? SuppliersModel
        if (supplier != null && supplier!!.name.isNotEmpty() && supplier!!.phone.isNotEmpty()) {
            // El objeto supplier contiene información
            //mostrar si es un item editable
        } else {
            // El objeto supplier es nulo o está vacío
            //saber si es que se creara un supplier nuevo
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNewSupplierBinding.inflate(inflater, container,false)

        binding.btnReturnNewSupplier.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    /*listener of the fragments to
    FloatingButton gone or visible*/
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