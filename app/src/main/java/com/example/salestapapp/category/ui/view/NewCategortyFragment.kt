package com.example.salestapapp.category.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.R
import com.example.salestapapp.category.data.CategoryRepository
import com.example.salestapapp.category.ui.viewmodel.NewCategortyViewModel
import com.example.salestapapp.databinding.FragmentNewCategortyBinding
import com.example.salestapapp.rom.CyberCoffeAppDatabase
import com.example.salestapapp.rom.CyberCoffeDatabase
import com.example.salestapapp.util.UtilsFunctions

class NewCategortyFragment : Fragment() {

    private var _binding: FragmentNewCategortyBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: CyberCoffeDatabase
    private lateinit var viewModel: NewCategortyViewModel
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

    }
}