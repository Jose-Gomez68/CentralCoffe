package com.example.salestapapp.user.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.user.data.domain.usecase.EditUserUseCase
import com.example.salestapapp.user.data.domain.usecase.GetUserByIDUseCase

class EditUserViewModelFactory(
    private val editUserUseCase: EditUserUseCase,
    private val getUserByIDUseCase: GetUserByIDUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditUserViewModel(editUserUseCase, getUserByIDUseCase) as T
    }

}