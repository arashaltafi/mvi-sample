package com.arash.altafi.mvisample.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<T> : ViewModel() {

    protected val _apiState = MutableStateFlow<ApiState<T>>(ApiState.Loading)
    val apiState: StateFlow<ApiState<T>> = _apiState

    protected fun <R> launchData(
        block: suspend () -> R,
        onSuccess: (R) -> Unit,
        onError: (String) -> Unit = { _apiState.value = ApiState.Error(it) }
    ) {
        viewModelScope.launch {
            try {
                val result = block()
                onSuccess(result)
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Unknown Error")
            }
        }
    }
}