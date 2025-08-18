package com.example.salestapapp.login.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestapapp.login.data.domain.GetUserLoginUseCase
import com.example.salestapapp.login.data.model.UsersModel
import kotlinx.coroutines.launch

class LoginUserViewModel(
    private val getUserByIDUseCase: GetUserLoginUseCase
): ViewModel() {

    private val _loginUserModel = MutableLiveData<UsersModel>()
    val loginUserModel: LiveData<UsersModel> = _loginUserModel

    fun getUser(userName: String, password: String) {
        viewModelScope.launch {
            val result = getUserByIDUseCase.invoke(userName, password)
            _loginUserModel.postValue(result)
        }
    }

}