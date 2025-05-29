package com.example.salestapapp.category.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.salestapapp.R
import com.example.salestapapp.category.data.CategoryRepository
import com.example.salestapapp.category.data.model.CategoryModel
import com.example.salestapapp.databinding.FragmentCategoryBinding
import com.example.salestapapp.databinding.FragmentEditCategoryBinding
import com.example.salestapapp.databinding.FragmentEditProductBinding
import com.example.salestapapp.products.data.ProductsRepository
import com.example.salestapapp.products.ui.viewmodel.EditProductViewModel
import com.example.salestapapp.rom.CyberCoffeAppDatabase
import com.example.salestapapp.rom.CyberCoffeDatabase
import com.example.salestapapp.util.UtilsFunctions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditCategoryFragment : Fragment() {

    private lateinit var _binding: FragmentEditCategoryBinding
    private val binding get() = _binding!!
    private lateinit var db: CyberCoffeDatabase
    //private lateinit var viewModel: EditProductViewModel
    private lateinit var utilsFunctions: UtilsFunctions
    private var categoryID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = CyberCoffeAppDatabase.CyberCoffeAppDatabase.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditCategoryBinding.inflate(inflater, container,false)
        val repository: CategoryRepository = CategoryRepository(db)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryID = arguments?.getInt("categoryID") ?: return
        utilsFunctions = UtilsFunctions()
        binding.btnReturnEditCategory.setOnClickListener {
            utilsFunctions.showConfirmDialog(requireActivity(),
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

        binding.btnEditCategory.setOnClickListener {
            if (validationsForms()){
                editCategory()
            }
        }

    }

    private fun editCategory() {

        val category = CategoryModel(
            categoryID,
            binding.etCategoryNameEdit.text.toString(),
            utilsFunctions.getCurrentFormattedDate(),
            utilsFunctions.getCurrentFormattedDate()
        )

        binding.pgSaveEditCategory.visibility = View.VISIBLE
        //viewModel.onCreate(category)
    }

    private fun validationsForms(): Boolean {

        val etEmpty = "El campo no puede ser vacio"
        if (binding.etCategoryNameEdit.text.toString().isEmpty()) {
            binding.etCategoryNameEdit.error = etEmpty
            return false
        } else if (binding.etCategoryNameEdit.text.toString().length <= 2) {
            binding.etCategoryNameEdit.error = "El nombre no puede ser 2 caracteres"
            return false
        }

        binding.etCategoryNameEdit.error = null
        return true
    }

}