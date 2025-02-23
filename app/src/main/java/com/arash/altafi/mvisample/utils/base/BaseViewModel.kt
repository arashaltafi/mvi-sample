package com.arash.altafi.mvisample.utils.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    // A shared flow to emit error messages (could be used for showing toasts, etc.)
    protected val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error.asSharedFlow()

    protected fun handleError(e: Throwable) {
        viewModelScope.launch {
            _error.emit(e.localizedMessage ?: "An error occurred")
        }
    }
}