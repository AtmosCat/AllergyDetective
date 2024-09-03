package com.example.allergydetective.presentation

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allergydetective.data.model.food.Food
import com.example.allergydetective.data.model.user.GroupMember
import com.example.allergydetective.data.model.user.User
import com.example.allergydetective.presentation.base.UiState
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class UserViewModel () : ViewModel() {

    private val _uiState: MutableLiveData<UiState<Any>> = MutableLiveData()
    val uiState: LiveData<UiState<Any>> = _uiState

    private val _users = MutableLiveData<MutableList<User>>()
    val users : LiveData<MutableList<User>> get() = _users

    private val _user = MutableLiveData<User?>()
    val user : LiveData<User?> get() = _user

    private val _currentUser = MutableLiveData<User?>()
    val currentUser : LiveData<User?> get() = _currentUser

    private val _currentUserAllergies = MutableLiveData<MutableList<String>?>()
    val currentUserAllergies : LiveData<MutableList<String>?> get() = _currentUserAllergies

    private val _currentUserFavorites = MutableLiveData<MutableList<Food>?>()
    val currentUserFavorites : LiveData<MutableList<Food>?> get() = _currentUserFavorites

    fun addUser(user: User) {
//        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                val newUsers = _users.value?: mutableListOf()
                newUsers.add(user)
                _users.value = newUsers
                // 보통은 변수로 LiveData.value 에 새로운 값을 써줌

//                _uiState.value = UiState.Success(_homeFoods)
            }.onFailure {
                Log.e(TAG, "addUser() failed! : ${it.message}")
                handleException(it)
//                _uiState.value = UiState.Error("Error")
            }
        }
    }

    fun getUser(_id: String) {
//        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                val currentUser =  _users.value?.find { it.id == _id }
                _user.value = currentUser
//                _uiState.value = UiState.Success(_homeFoods)
            }.onFailure {
                Log.e(TAG, "getHomeFoods() failed! : ${it.message}")
                handleException(it)
//                _uiState.value = UiState.Error("Error")
            }
        }
    }

    fun setCurrentUser(user: User) {
//        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                var newUser = _currentUser.value
                newUser = user
                _currentUser.value = newUser
                // 보통은 변수로 LiveData.value 에 새로운 값을 써줌

//                _uiState.value = UiState.Success(_homeFoods)
            }.onFailure {
                Log.e(TAG, "setUser() failed! : ${it.message}")
                handleException(it)
//                _uiState.value = UiState.Error("Error")
            }
        }
    }

    fun setCurrentUserAllergy(allergies: MutableList<String>) {
//        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                var newSelectedAllergies: MutableList<String>? = _currentUser.value?.allergy
                newSelectedAllergies = allergies
                _currentUser.value?.allergy = newSelectedAllergies
//                _uiState.value = UiState.Success("Example")

            }.onFailure {
                Log.e(TAG, "setAllergiesFilter() failed! : ${it.message}")
                handleException(it)
//                _uiState.value = UiState.Error("Error")
            }
        }
    }

    fun addAllergy(allergen: String) {
//        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                val newSelectedAllergies: MutableList<String>? = _currentUser.value?.allergy
                newSelectedAllergies?.add(allergen)
                if (newSelectedAllergies != null) {
                    _currentUser.value?.allergy = newSelectedAllergies
                }
//                _uiState.value = UiState.Success("Example")

            }.onFailure {
                Log.e(TAG, "setAllergiesFilter() failed! : ${it.message}")
                handleException(it)
//                _uiState.value = UiState.Error("Error")
            }
        }
    }

    fun addFavorite(food: Food) {
//        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                var newLikedFood: MutableList<Food>? = _currentUser.value?.like
                newLikedFood?.add(food)
                _currentUser.value?.like = newLikedFood!!
//                _uiState.value = UiState.Success("Example")

            }.onFailure {
                Log.e(TAG, "addFavorite() failed! : ${it.message}")
                handleException(it)
//                _uiState.value = UiState.Error("Error")
            }
        }
    }

    fun removeFavorite(food: Food) {
//        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                var newLikedFood: MutableList<Food>? = _currentUser.value?.like
                newLikedFood?.remove(food)
                _currentUser.value?.like = newLikedFood!!
//                _uiState.value = UiState.Success("Example")
            }.onFailure {
                Log.e(TAG, "removeFavorite() failed! : ${it.message}")
                handleException(it)
//                _uiState.value = UiState.Error("Error")
            }
        }
    }

    fun setGroupMember(members: MutableList<GroupMember>) {
        //        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                var newMembers: MutableList<GroupMember>?
                newMembers = members
                _currentUser.value!!.group = newMembers
//                _uiState.value = UiState.Success("Example")
            }.onFailure {
                Log.e(TAG, "setGroupMember() failed! : ${it.message}")
                handleException(it)
//                _uiState.value = UiState.Error("Error")
            }
        }
    }

    fun editGroupMemberInfo(position: Int, member: GroupMember) {
        //        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                var newMember = _currentUser.value!!.group[position]
                newMember = member
                _currentUser.value!!.group[position] = newMember
//                _uiState.value = UiState.Success("Example")
            }.onFailure {
                Log.e(TAG, "setGroupMember() failed! : ${it.message}")
                handleException(it)
//                _uiState.value = UiState.Error("Error")
            }
        }
    }


    private fun handleException(e: Throwable) {
        when (e) {
            is HttpException -> {
                val errorJsonString = e.response()?.errorBody()?.string()
                Log.e(TAG, "HTTP error: $errorJsonString")
            }

            is IOException -> Log.e(TAG, "Network error: $e")
            else -> Log.e(TAG, "Unexpected error: $e")
        }
    }
}