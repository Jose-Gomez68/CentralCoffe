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
import com.example.salestapapp.category.data.domain.InsertCategoryUseCase
import com.example.salestapapp.category.data.model.CategoryModel
import com.example.salestapapp.category.ui.viewmodel.NewCategoryViewModel
import com.example.salestapapp.category.ui.viewmodel.NewCategoryViewModelFactory
import com.example.salestapapp.databinding.FragmentNewCategortyBinding
import com.example.salestapapp.products.ui.view.OnFragmentChangedListener
import com.example.salestapapp.rom.CyberCoffeAppDatabase
import com.example.salestapapp.rom.CyberCoffeDatabase
import com.example.salestapapp.util.UtilsFunctions

class NewCategortyFragment : Fragment() {

    private var _binding: FragmentNewCategortyBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: CyberCoffeDatabase
    private lateinit var viewModel: NewCategoryViewModel
    private var nameCategory = ""
    private var listener: OnCategoryFragmentChangeListener? = null
    private lateinit var utilsFunctions: UtilsFunctions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = CyberCoffeAppDatabase.CyberCoffeAppDatabase.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewCategortyBinding.inflate(inflater, container, false)
        val repository: CategoryRepository = CategoryRepository(db)
        val viewModelProviderFactory = NewCategoryViewModelFactory(InsertCategoryUseCase(repository))
        viewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        )[NewCategoryViewModel::class.java]

        binding.btnReturnNewCategory.setOnClickListener {
            utilsFunctions.showConfirmDialog(requireContext(),
                getString(R.string.title_message_return_view_util),
                getString(R.string.message_return_view_util),
                getString(R.string.exit_message_return_view_util),
                getString(R.string.cancel_message_return_view_util),
                onConfirm = {
                    requireActivity().onBackPressed()
                },
                onCancel = {

                }
            )
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        utilsFunctions = UtilsFunctions()

        binding.btnNewCategory.setOnClickListener {
            if (validationsForms()){
                saveCategory()
            }
        }

        viewModel.newCategoryModel.observe(viewLifecycleOwner) { result ->
            Log.e("SE INSERTO", "" + result.name)
            Log.e("SE INSERTO", "" + result.createDate)
            if (result.name.isNotEmpty()){
                binding.etNewCategoryName.setText("")
                binding.pgSaveNewCategory.visibility = View.GONE
            }
        }

    }

    private fun saveCategory() {

        val category = CategoryModel(
            0,
            binding.etNewCategoryName.text.toString(),
            utilsFunctions.getCurrentFormattedDate(),
            utilsFunctions.getCurrentFormattedDate()
        )

        binding.pgSaveNewCategory.visibility = View.VISIBLE
        viewModel.onCreate(category)
    }

    private fun validationsForms(): Boolean {

        val etEmpty = "El campo no puede ser vacio"
        if (binding.etNewCategoryName.text.toString().isEmpty()) {
            binding.etNewCategoryName.error = etEmpty
            return false
        } else if (binding.etNewCategoryName.text.toString().length <= 2) {
            binding.etNewCategoryName.error = "El nombre no puede ser 2 caracteres"
            return false
        }

        binding.etNewCategoryName.error = null
        return true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCategoryFragmentChangeListener) {
            listener = context
            listener?.onCategoryFragmentChangeListener(this)
        } else {
            throw RuntimeException("$context must implement onCategoryFragmentChangeListener")
        }
    }

}