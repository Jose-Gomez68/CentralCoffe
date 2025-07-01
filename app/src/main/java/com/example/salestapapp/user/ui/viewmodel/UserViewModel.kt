package com.example.salestapapp.user.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestapapp.login.data.model.UsersModel
import com.example.salestapapp.user.data.domain.usecase.DeleteUserByIDUseCase
import com.example.salestapapp.user.data.domain.usecase.GetUsersUseCase
import kotlinx.coroutines.launch

class UserViewModel(
    private val getUsers: GetUsersUseCase,
    private val deleteUser: DeleteUserByIDUseCase
): ViewModel() {

    private val _usersModel = MutableLiveData<List<UsersModel>>()
    val usersModel: LiveData<List<UsersModel>> = _usersModel

    fun onCreate() {
        viewModelScope.launch {
            val result = getUsers.invoke()
            Log.e("AQUII1",  result.toString())
            if (!result.isNullOrEmpty()){
                _usersModel.postValue(result)
            }
        }
    }

    fun removeUser(user: UsersModel){
        viewModelScope.launch {
            deleteUser.invoke(user)
            val currentList = _usersModel.value.orEmpty().toMutableList()
            currentList.remove(user)
            Log.e("Eliminando usuario", "${currentList.size}")
        }
    }

}