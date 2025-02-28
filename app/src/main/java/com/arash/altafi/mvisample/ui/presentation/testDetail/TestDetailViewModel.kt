package com.arash.altafi.mvisample.ui.presentation.testDetail

import androidx.lifecycle.viewModelScope
import com.arash.altafi.mvisample.data.model.TestDetailEntity
import com.arash.altafi.mvisample.data.repository.TestRepository
import com.arash.altafi.mvisample.ui.base.ApiState
import com.arash.altafi.mvisample.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestDetailViewModel @Inject constructor(
    private val repository: TestRepository
) : BaseViewModel<TestDetailEntity>() {

    fun loadUserDetail(id: String) {
        _apiState.value = ApiState.Loading
        viewModelScope.launch {
            try {
                val user = repository.getUserDetail(id)
                if (user != null) {
                    _apiState.value = ApiState.Success(user)
                } else {
                    _apiState.value = ApiState.Error("User not found")
                }
            } catch (e: Exception) {
                _apiState.value = ApiState.Error(e.localizedMessage ?: "Error loading detail")
            }
        }
    }
}