package com.example.salestapapp.menu.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MenuListViewModel: ViewModel() {

    fun invoke() {
        viewModelScope.launch{
            //logica
        }
    }

}