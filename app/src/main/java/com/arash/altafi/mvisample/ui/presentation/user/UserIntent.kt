package com.arash.altafi.mvisample.ui.presentation.user

sealed class UserIntent {
    data class FetchUsers(val pageNumber: Int, val pageSize: Int) : UserIntent()
}