package com.example.salestapapp.user.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.user.data.domain.usecase.DeleteUserByIDUseCase
import com.example.salestapapp.user.data.domain.usecase.GetUsersUseCase

class UserViewModelFactory(
    private val getUser: GetUsersUseCase,
    private val deleteUser: DeleteUserByIDUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(getUser, deleteUser) as T
    }

}