package com.example.salestapapp.user.ui.view

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
import com.example.salestapapp.databinding.FragmentUserBinding
import com.example.salestapapp.login.data.UserRepository
import com.example.salestapapp.login.data.model.UsersModel
import com.example.salestapapp.rom.CyberCoffeAppDatabase
import com.example.salestapapp.rom.CyberCoffeDatabase
import com.example.salestapapp.user.data.domain.usecase.DeleteUserByIDUseCase
import com.example.salestapapp.user.data.domain.usecase.GetUsersUseCase
import com.example.salestapapp.user.ui.viewmodel.UserViewModel
import com.example.salestapapp.user.ui.viewmodel.UserViewModelFactory
import com.example.salestapapp.util.UtilsFunctions

class UserFragment : Fragment() {

    private lateinit var utilsFunctions: UtilsFunctions
    private lateinit var binding: FragmentUserBinding
    private var listener: OnUserFragmentChangeListener? = null
    private lateinit var userAdapter: UserListAdapter
    private lateinit var viewModel: UserViewModel
    private lateinit var db: CyberCoffeDatabase
    private var fullUser: List<UsersModel> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = CyberCoffeAppDatabase.CyberCoffeAppDatabase.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        val repository = UserRepository(db)
        val viewModelProviderFactory = UserViewModelFactory(
            GetUsersUseCase(repository),
            DeleteUserByIDUseCase(repository)
        )

        utilsFunctions = UtilsFunctions()
        viewModel = ViewModelProvider(
            this, viewModelProviderFactory
        )[UserViewModel::class.java]
        viewModel.onCreate()
        binding.btnReturnUser.setOnClickListener {
            requireActivity().onBackPressed()
        }

        recyclerViewInit()

        binding.searchVUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!::userAdapter.isInitialized) return false
                val filteredList = if (newText.isNullOrBlank()) {
                    fullUser
                } else {
                    fullUser.filter {
                        val fullName = "${it.name} ${it.lastName}".trim()
                        fullName.contains(newText.trim(), ignoreCase = true)
                    }
                }
                userAdapter.updateList(filteredList)
                return true
            }

        })

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnUserFragmentChangeListener) {
            listener = context
            listener?.onUserFragmentChangeListener(this)
        } else {
            throw RuntimeException("$context must implement OnUserFragmentChangeListener")
        }
    }

    private fun recyclerViewInit() {
        viewModel.usersModel.observe(viewLifecycleOwner) { result ->
            fullUser = result
            userAdapter = UserListAdapter(
                result,
                onItemRemove = { user ->
                    utilsFunctions.deleteDialog(
                        requireContext(),
                        getString(R.string.title_dialog_delete_user, user.name),
                        getString(R.string.message_dialog_delete_user),
                        "Eliminar",
                        "Cancelar",
                        onConfirm = {
                            viewModel.removeUser(user)
                            userAdapter.updateList(result)
                        }
                    )
                },
                onItemGoEdit = { user ->

                    val bundle = Bundle().apply {
                        putInt("userID", user.id)
                    }

                    val editUser = EditUserFragment()
                    editUser.arguments = bundle
                    val transaction = requireFragmentManager().beginTransaction()
                    transaction.replace(R.id.userContainerFragment, editUser)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
            )

            binding.rvUserFrag.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = userAdapter
            }

            if (result.isEmpty()){
                binding.rvUserFrag.visibility = View.INVISIBLE
                binding.tvNoDataListUser.visibility = View.VISIBLE
            }else{
                binding.rvUserFrag.visibility = View.VISIBLE
                binding.tvNoDataListUser.visibility = View.GONE
            }
        }
    }

}