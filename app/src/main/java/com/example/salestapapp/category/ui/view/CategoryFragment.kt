package com.example.salestapapp.category.ui.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.R
import com.example.salestapapp.category.data.CategoryRepository
import com.example.salestapapp.category.data.domain.DeleteCategoryByIdUseCase
import com.example.salestapapp.category.data.domain.GetCategoryUseCase
import com.example.salestapapp.category.ui.CategoryListAdapter
import com.example.salestapapp.category.ui.viewmodel.CategoryListViewModel
import com.example.salestapapp.category.ui.viewmodel.CategoryListViewModelFactory
import com.example.salestapapp.databinding.FragmentCategoryBinding
import com.example.salestapapp.rom.CyberCoffeAppDatabase
import com.example.salestapapp.rom.CyberCoffeDatabase
import com.example.salestapapp.util.UtilsFunctions


class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoryAdap: CategoryListAdapter
    private lateinit var db: CyberCoffeDatabase
    private lateinit var viewModel: CategoryListViewModel
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
        val repository: CategoryRepository = CategoryRepository(db)
        val viewModelProviderFactory = CategoryListViewModelFactory(GetCategoryUseCase(repository),
            DeleteCategoryByIdUseCase(repository)
        )
        viewModel = ViewModelProvider(
            this,viewModelProviderFactory
        )[CategoryListViewModel::class.java]
        viewModel.onCreate()

        binding.btnReturnCategory.setOnClickListener {
            requireActivity().onBackPressed()
        }

        viewModel.categoryModel.observe(viewLifecycleOwner) { result ->
            categoryAdap = CategoryListAdapter(result,
                onItemRemove = { category ->
                    util.deleteDialog(
                        requireContext(),
                        getString(R.string.title_dialog_delete_category, category.name),
                        getString(R.string.message_dialog_delete_category),
                        "Eliminar",
                        "Cancelar",
                        onConfirm = {
                            viewModel.removeCategory(category)
                            categoryAdap.updateList(result)
                        }
                    )
                    Log.e("CATEGORIA ELIMINADA", ""+result.size)
                },
                onItemGoEdit = { category ->
                    val bundle = Bundle().apply {
                        putInt("categoryID", category.id)
                    }

                    val nuevoFragmento = EditCategoryFragment()
                    nuevoFragmento.arguments = bundle

                    val transaction = requireFragmentManager().beginTransaction()
                    transaction.replace(R.id.categoryContainerFragment, nuevoFragmento)
                    transaction.addToBackStack(null)
                    transaction.commit()

                })
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
       /* if (context is OnCategoryFragmentChangeListener) {
            listener = context
            listener?.onCategoryFragmentChangeListener(this)
        } else {
            throw RuntimeException("$context must implement OnCategoryFragmentChangeListener")
        }*/
    }
}