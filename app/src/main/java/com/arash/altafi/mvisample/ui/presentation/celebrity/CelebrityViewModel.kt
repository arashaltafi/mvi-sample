package com.arash.altafi.mvisample.ui.presentation.celebrity

import androidx.lifecycle.viewModelScope
import com.arash.altafi.mvisample.data.repository.CelebrityRepository
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
class CelebrityViewModel @Inject constructor(
    private val repository: CelebrityRepository
) : BaseViewModel() {

    private val _state = MutableStateFlow<CelebrityState>(CelebrityState.Idle)
    val state: StateFlow<CelebrityState> = _state.asStateFlow()

    private val intentChannel = Channel<CelebrityIntent>(Channel.UNLIMITED)

    init {
        handleIntents()
    }

    fun sendIntent(intent: CelebrityIntent) {
        viewModelScope.launch {
            intentChannel.send(intent)
        }
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect { intent ->
                when (intent) {
                    is CelebrityIntent.FetchCelebrities -> fetchCelebrities(intent.pageNumber, intent.pageSize)
                }
            }
        }
    }

    private fun fetchCelebrities(pageNumber: Int, pageSize: Int) {
        _state.value = CelebrityState.Loading
        viewModelScope.launch {
            repository.getCelebrities(pageNumber, pageSize)
                .catch { e ->
                    _state.value = CelebrityState.Error(e.localizedMessage ?: "Unknown error")
                    handleError(e)
                }
                .collect { response ->
                    if (response.isSuccessful) {
                        response.body()?.let { celebrities ->
                            _state.value = CelebrityState.Success(celebrities)
                        } ?: run {
                            _state.value = CelebrityState.Error("Empty response")
                        }
                    } else {
                        _state.value = CelebrityState.Error("Error: ${response.code()}")
                    }
                }
        }
    }
}
