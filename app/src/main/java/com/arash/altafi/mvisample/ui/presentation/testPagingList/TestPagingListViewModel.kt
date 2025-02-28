package com.arash.altafi.mvisample.ui.presentation.testPagingList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arash.altafi.mvisample.data.model.TestDetailEntity
import com.arash.altafi.mvisample.data.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestPagingListViewModel @Inject constructor(
    private val repository: TestRepository
) : ViewModel() {
    private val _pagingData = MutableStateFlow<PagingData<TestDetailEntity>>(PagingData.empty())
    val pagingData: StateFlow<PagingData<TestDetailEntity>> = _pagingData

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            repository.getUserPaging(pageSize = 10)
                .cachedIn(viewModelScope)
                .collectLatest {
                    _pagingData.value = it
                }
        }
    }
}