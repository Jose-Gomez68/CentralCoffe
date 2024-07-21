package com.example.salestapapp.supplier.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.salestapapp.R
import com.example.salestapapp.databinding.FragmentSupplierBinding
import com.example.salestapapp.rom.CyberCoffeAppDatabase
import com.example.salestapapp.rom.CyberCoffeDatabase
import com.example.salestapapp.supplier.data.SupplierRepository
import com.example.salestapapp.supplier.data.domain.DeleteSupplierByIDUseCase
import com.example.salestapapp.supplier.data.domain.GetSuppliersUseCase
import com.example.salestapapp.supplier.data.model.SuppliersModel
import com.example.salestapapp.supplier.data.viewmodel.SupplierViewModel
import com.example.salestapapp.supplier.data.viewmodel.SupplierViewModelFactory

class SupplierFragment : Fragment() {

    private lateinit var binding: FragmentSupplierBinding
    private var listener: OnSupplierFragmentChangeListener? = null
    private lateinit var supplierAdap: SupplierListAdapter

    private lateinit var viewModel: SupplierViewModel
    private lateinit var db: CyberCoffeDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = CyberCoffeAppDatabase.CyberCoffeAppDatabase.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSupplierBinding.inflate(inflater, container,false)

        val repository = SupplierRepository(db)
        val viewModelProviderFactory = SupplierViewModelFactory(GetSuppliersUseCase(repository),
            DeleteSupplierByIDUseCase(repository)
        )
        viewModel = ViewModelProvider(
            this,viewModelProviderFactory
        )[SupplierViewModel::class.java]
        viewModel.onCreate()
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

    private fun recyclerViewInit() {
        viewModel.supplierModel.observe(viewLifecycleOwner) { result ->
            supplierAdap = SupplierListAdapter(
                result,
                onItemRemove = { supplier ->
                    deleteDialog(supplier, result)
                },
                onItemGoEdit = { supplier ->
                    val editSupplier = NewSupplierFragment()
                    val transaction = requireFragmentManager().beginTransaction()
                    transaction.replace(R.id.supplierContainerFragment, editSupplier)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
            )

            binding.rvSuppFragSupp.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = supplierAdap
            }

            if (result.isEmpty()){
                binding.rvSuppFragSupp.visibility = View.INVISIBLE
                binding.tvNoDataListSupp.visibility = View.VISIBLE
            }else{
                binding.rvSuppFragSupp.visibility = View.VISIBLE
                binding.tvNoDataListSupp.visibility = View.INVISIBLE
            }
        }
    }

    private fun deleteDialog(it: SuppliersModel, result: List<SuppliersModel>){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("")
        builder.setMessage("")

        builder.setPositiveButton("Eliminar") { dialog, which ->
            viewModel.removeSuppliers(it)
            //mi adaptador para borrar el listado
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.dismiss()
            dialog.cancel()
        }
        builder.show()

    }

}