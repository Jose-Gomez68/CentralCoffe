package com.example.salestapapp.login.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.login.data.domain.GetUserLoginUseCase

class LoginUserViewModelFactory(
    private val getUserLoginUseCase: GetUserLoginUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginUserViewModel(getUserLoginUseCase) as T
    }
}