package com.arash.altafi.mvisample.ui.presentation.user

import com.arash.altafi.mvisample.data.model.UserResponse

sealed class UserState {
    object Idle : UserState()
    object Loading : UserState()
    data class Success(val users: List<UserResponse>) : UserState()
    data class Error(val message: String) : UserState()
}