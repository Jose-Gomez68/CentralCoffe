package com.example.salestapapp.user.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.user.data.domain.usecase.SaveUserUseCase

class NewUserViewModelFactory(
    private val saveUserUseCase: SaveUserUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewUserViewModel(saveUserUseCase) as T
    }

}