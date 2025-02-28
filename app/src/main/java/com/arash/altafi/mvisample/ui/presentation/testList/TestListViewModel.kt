package com.arash.altafi.mvisample.ui.presentation.testList

import androidx.lifecycle.viewModelScope
import com.arash.altafi.mvisample.data.model.TestDetailEntity
import com.arash.altafi.mvisample.data.repository.TestRepository
import com.arash.altafi.mvisample.ui.base.ApiState
import com.arash.altafi.mvisample.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestListViewModel @Inject constructor(
    private val repository: TestRepository
) : BaseViewModel<List<TestDetailEntity>>() {

    init {
        loadUsers()
    }

    fun loadUsers() {
        _apiState.value = ApiState.Loading
        viewModelScope.launch {
            try {
                val users = repository.getUserList() ?: emptyList()
                _apiState.value = ApiState.Success(users)
            } catch (e: Exception) {
                _apiState.value = ApiState.Error(e.localizedMessage ?: "Error loading users")
            }
        }
    }
}