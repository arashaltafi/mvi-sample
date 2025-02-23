package com.arash.altafi.mvisample.ui.presentation.user

import androidx.lifecycle.viewModelScope
import com.arash.altafi.mvisample.data.repository.UserRepository
import com.arash.altafi.mvisample.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : BaseViewModel() {

    private val _state = MutableStateFlow<UserState>(UserState.Idle)
    val state: StateFlow<UserState> = _state.asStateFlow()

    private val intentChannel = Channel<UserIntent>(Channel.UNLIMITED)

    init {
        handleIntents()
    }

    fun sendIntent(intent: UserIntent) {
        viewModelScope.launch {
            intentChannel.send(intent)
        }
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect { intent ->
                when (intent) {
                    is UserIntent.FetchUsers -> fetchUsers(intent.pageNumber, intent.pageSize)
                }
            }
        }
    }

    private fun fetchUsers(pageNumber: Int, pageSize: Int) {
        _state.value = UserState.Loading
        viewModelScope.launch {
            repository.getUsers(pageNumber, pageSize)
                .catch { e ->
                    _state.value = UserState.Error(e.localizedMessage ?: "Unknown error")
                    handleError(e)
                }
                .collect { response ->
                    if (response.isSuccessful) {
                        response.body()?.let { users ->
                            _state.value = UserState.Success(users)
                        } ?: run {
                            _state.value = UserState.Error("Empty response")
                        }
                    } else {
                        _state.value = UserState.Error("Error: ${response.code()}")
                    }
                }
        }
    }
}
