package com.example.salestapapp.user.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestapapp.login.data.model.UsersModel
import com.example.salestapapp.user.data.domain.usecase.EditUserUseCase
import com.example.salestapapp.user.data.domain.usecase.GetUserByIDUseCase
import kotlinx.coroutines.launch

class EditUserViewModel(
    private val editUser: EditUserUseCase,
    private val getUserByIDUseCase: GetUserByIDUseCase
): ViewModel() {

    private val _editUserModel = MutableLiveData<UsersModel>()
    val editUserModel: LiveData<UsersModel> = _editUserModel

    private val _editUserSucces = MutableLiveData<UsersModel>()
    val editUserSucces: LiveData<UsersModel> = _editUserSucces

    fun onUpdate (usersModel: UsersModel) {
        viewModelScope.launch {
            val result = editUser.invoke(usersModel)
            _editUserSucces.postValue(result)
        }
    }

    fun getUser(userID: Int) {
        viewModelScope.launch {
            val result = getUserByIDUseCase.invoke(userID)
            _editUserModel.postValue(result)
        }
    }

}