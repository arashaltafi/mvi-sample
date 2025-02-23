package com.arash.altafi.mvisample.ui.presentation.celebrity

import com.arash.altafi.mvisample.data.model.CelebrityResponse

sealed class CelebrityState {
    object Idle : CelebrityState()
    object Loading : CelebrityState()
    data class Success(val celebrities: List<CelebrityResponse>) : CelebrityState()
    data class Error(val message: String) : CelebrityState()
}