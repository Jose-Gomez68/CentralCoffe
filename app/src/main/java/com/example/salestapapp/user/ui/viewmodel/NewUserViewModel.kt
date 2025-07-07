package com.example.salestapapp.user.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestapapp.login.data.model.UsersModel
import com.example.salestapapp.user.data.domain.usecase.SaveUserUseCase
import kotlinx.coroutines.launch

class NewUserViewModel(
    private val saveUserUseCase: SaveUserUseCase
): ViewModel() {

    private val _newUserModel = MutableLiveData<UsersModel>()
    val newUserModel: LiveData<UsersModel> = _newUserModel

    fun onCreate(usersModel: UsersModel) {
        viewModelScope.launch {
            val result = saveUserUseCase.invoke(usersModel)
            _newUserModel.postValue(result)
        }
    }

}