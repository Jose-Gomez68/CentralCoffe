package com.example.salestapapp.supplier.ui.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.salestapapp.R
import com.example.salestapapp.databinding.FragmentSupplierBinding
import com.example.salestapapp.rom.CyberCoffeAppDatabase
import com.example.salestapapp.rom.CyberCoffeDatabase
import com.example.salestapapp.supplier.data.domain.repository.SupplierRepository
import com.example.salestapapp.supplier.data.domain.usecase.DeleteSupplierByIDUseCase
import com.example.salestapapp.supplier.data.domain.usecase.GetSuppliersUseCase
import com.example.salestapapp.supplier.data.model.SuppliersModel
import com.example.salestapapp.supplier.ui.viewmodel.SupplierViewModel
import com.example.salestapapp.supplier.ui.viewmodel.SupplierViewModelFactory
import com.example.salestapapp.util.UtilsFunctions

class SupplierFragment : Fragment() {

    private lateinit var utilsFunctions: UtilsFunctions
    private lateinit var binding: FragmentSupplierBinding
    private var listener: OnSupplierFragmentChangeListener? = null
    private lateinit var supplierAdap: SupplierListAdapter

    private lateinit var viewModel: SupplierViewModel
    private lateinit var db: CyberCoffeDatabase
    private var fullSupplier: List<SuppliersModel> = emptyList()

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
        val viewModelProviderFactory = SupplierViewModelFactory(
            GetSuppliersUseCase(repository),
            DeleteSupplierByIDUseCase(repository)
        )
        utilsFunctions = UtilsFunctions()
        viewModel = ViewModelProvider(
            this,viewModelProviderFactory
        )[SupplierViewModel::class.java]
        viewModel.onCreate()
        binding.btnReturnSupp.setOnClickListener {
            requireActivity().onBackPressed()
        }

        recyclerViewInit()

        binding.searchVSupplier.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = if (newText.isNullOrBlank()) {
                    fullSupplier
                } else {
                    fullSupplier.filter {
                        it.name.contains(newText.trim(), ignoreCase = true)
                    }
                }
                supplierAdap.updateList(filteredList)
                return true
            }

        })

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
            fullSupplier = result
            supplierAdap = SupplierListAdapter(
                result,
                onItemRemove = { supplier ->
                    utilsFunctions.deleteDialog(
                        requireContext(),
                        getString(R.string.title_dialog_delete_supplier, supplier.name),
                        getString(R.string.message_dialog_delete_supplier),
                        "Eliminar",
                        "Cancelar",
                        onConfirm = {
                            viewModel.removeSuppliers(supplier)
                            supplierAdap.updateList(result)
                        }
                    )
                },
                onItemGoEdit = { supplier ->

                    val bundle = Bundle().apply {
                        putInt("supplierID", supplier.id)
                    }

                    val editSupplier = EditSupplierFragment()
                    editSupplier.arguments = bundle
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

}