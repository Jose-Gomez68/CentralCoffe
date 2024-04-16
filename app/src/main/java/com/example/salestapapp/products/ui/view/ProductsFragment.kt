package com.example.salestapapp.products.ui.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.salestapapp.R
import com.example.salestapapp.databinding.FragmentProductsBinding
import com.example.salestapapp.products.data.ProductsRepository
import com.example.salestapapp.products.data.domain.DeleteProductByIdUseCase
import com.example.salestapapp.products.data.domain.GetProductsUseCase
import com.example.salestapapp.products.data.model.ProductModel
import com.example.salestapapp.products.ui.ProductListAdapter
import com.example.salestapapp.products.ui.viewmodel.ProductListViewModelFactory
import com.example.salestapapp.products.ui.viewmodel.ProductsListViewModel
import com.example.salestapapp.rom.CyberCoffeAppDatabase
import com.example.salestapapp.rom.CyberCoffeDatabase

class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdap: ProductListAdapter
    private lateinit var db: CyberCoffeDatabase
    private lateinit var viewModel: ProductsListViewModel
    private var listener: OnFragmentChangedListener? = null

   /* var products = listOf(
        ProductModel(1,"coca cola", 10.0F,20.3,"", 1,"refrescos",1, "cocacola sa",1, "pz", "23/03/2024"),
        ProductModel(2,"coca cola2", 10.0F,20.3,"", 1,"refrescos",1, "cocacola sa",1, "pz", "23/03/2024"),
        ProductModel(3,"coca cola3", 10.0F,20.3,"", 1,"refrescos",1, "cocacola sa",1, "pz", "23/03/2024"),
        ProductModel(4,"coca cola4", 10.0F,20.3,"", 1,"refrescos",1, "cocacola sa",1, "pz", "23/03/2024"),
        ProductModel(5,"coca cola5", 10.0F,20.3,"", 1,"refrescos",1, "cocacola sa",1, "pz", "23/03/2024"),
        ProductModel(6,"coca cola6", 10.0F,20.3,"", 1,"refrescos",1, "cocacola sa",1, "pz", "23/03/2024"),
        ProductModel(7,"coca cola7", 10.0F,20.3,"", 1,"refrescos",1, "cocacola sa",1, "pz", "23/03/2024"),
        ProductModel(8,"coca cola8", 10.0F,20.3,"", 1,"refrescos",1, "cocacola sa",1, "pz", "23/03/2024"),
        ProductModel(9,"coca cola9", 10.0F,20.3,"", 1,"refrescos",1, "cocacola sa",1, "pz", "23/03/2024"),
        ProductModel(10,"coca cola10", 10.0F,20.3,"", 1,"refrescos",1, "cocacola sa",1, "pz", "23/03/2024"),
        ProductModel(11,"coca cola11", 10.0F,20.3,"", 1,"refrescos",1, "cocacola sa",1, "pz", "23/03/2024"),
    )*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = CyberCoffeAppDatabase.CyberCoffeAppDatabase.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val repository: ProductsRepository = ProductsRepository(db)
        val viewModelProviderFactory = ProductListViewModelFactory(GetProductsUseCase(repository),
            DeleteProductByIdUseCase(repository)
        )
        viewModel = ViewModelProvider(
            this,viewModelProviderFactory
        )[ProductsListViewModel::class.java]
        viewModel.onCreate()

        /*binding.fbAddProduct.setOnClickListener {
            *//*val ramdom = Random.nextInt(10000)
            val prod = ProductModel(ramdom,"coca cola$ramdom", 10.0F,20.3,"", 1,"refrescos",1, "cocacola sa",1, "pz", "23/03/2024")
            products = products.plus(prod)
            //productAdap.notifyDataSetChanged()
            productAdap.updateList(products)*//*
        }*/

        viewModel.productModel.observe(viewLifecycleOwner) { result ->

            /*productAdap = ProductListAdapter(result){
                deleteDialog(it, result)
                Log.e("ELIMINANDO1", ""+result.size)
            }*/
            productAdap = ProductListAdapter(result,
                onItemRemove = { product ->
                    deleteDialog(product, result)
                    Log.e("ELIMINANDO1", ""+result.size)
                },
                onItemGoEdit = { product ->
                    Log.e("MOVIENDOME", "A OTRO FRAGMENTO")
                    val nuevoFragmento = EditProductFragment()
                    val transaction = requireFragmentManager().beginTransaction()
                    transaction.replace(R.id.productsContainerFragment, nuevoFragmento)
                    transaction.addToBackStack(null)
                    transaction.commit()
                })

            binding.rvProductsFragProduct.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = productAdap
            }

            if (result.isEmpty()){
                binding.rvProductsFragProduct.visibility = View.INVISIBLE
                binding.tvNoDataListProduct.visibility = View.VISIBLE
            }else{
                binding.rvProductsFragProduct.visibility = View.VISIBLE
                binding.tvNoDataListProduct.visibility = View.INVISIBLE
            }

        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentChangedListener) {
            listener = context
            listener?.onFragmentChanged(this)
        } else {
            throw RuntimeException("$context must implement OnFragmentChangedListener")
        }
    }

    private fun deleteDialog(it: ProductModel, result: List<ProductModel>) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.title_dialog_delete_product, it.name))
        builder.setMessage(getString(R.string.message_dialog_delete_product))

        builder.setPositiveButton("Eliminar") { dialog, which ->
            viewModel.removeProduct(it)
            productAdap.updateList(result)
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.dismiss()
            dialog.cancel()
        }
        builder.show()
    }

}