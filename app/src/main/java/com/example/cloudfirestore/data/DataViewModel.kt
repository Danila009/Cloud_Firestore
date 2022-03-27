package com.example.cloudfirestore.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cloudfirestore.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DataViewModel(
    private val dataRepository: DataRepository
):ViewModel() {

    private val _responseUsers:MutableStateFlow<List<User>> = MutableStateFlow(listOf<User>())
    val responseUsers:StateFlow<List<User>> = _responseUsers.asStateFlow()

    fun addUser(user: User){
        viewModelScope.launch {
            dataRepository.addUser(user)
        }
    }

    fun getUsers(
        search:String = ""
    ){
        viewModelScope.launch {
            dataRepository.getUsers(search).collect{
                _responseUsers.value = it
            }
            Log.e("Firestore", _responseUsers.value.toString())
        }
    }

    fun updateUser(user: User){
        viewModelScope.launch {
            dataRepository.updateUser(user)
        }
    }

    fun deleteUser(user: User){
        viewModelScope.launch {
            dataRepository.deleteUser(user)
        }
    }

    fun updateUsername(username:String, document:String){
        viewModelScope.launch {
            dataRepository.updateUsername(
                username,
                document
            )
        }
    }
}