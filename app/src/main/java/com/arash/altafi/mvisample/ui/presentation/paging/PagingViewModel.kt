package com.arash.altafi.mvisample.ui.presentation.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arash.altafi.mvisample.data.model.UserResponse
import com.arash.altafi.mvisample.data.repository.PagingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PagingViewModel @Inject constructor(
    private val repository: PagingRepository
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _usersFlow = MutableStateFlow<PagingData<UserResponse>>(PagingData.empty())
    val usersFlow: StateFlow<PagingData<UserResponse>> = _usersFlow

    init {
        fetchUsers()
    }


    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _usersFlow.value = PagingData.empty()
            fetchUsers()
            _isRefreshing.value = false
        }
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            repository.getUsersPagingData(pageSize = 10)
                .cachedIn(viewModelScope)
                .collectLatest {
                    _usersFlow.value = it
                }
        }
    }
}