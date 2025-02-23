package com.arash.altafi.mvisample.ui.presentation.celebrity

sealed class CelebrityIntent {
    data class FetchCelebrities(val pageNumber: Int, val pageSize: Int) : CelebrityIntent()
}