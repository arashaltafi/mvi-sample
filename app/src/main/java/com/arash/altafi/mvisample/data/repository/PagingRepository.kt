package com.arash.altafi.mvisample.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.arash.altafi.mvisample.data.api.PagingService
import com.arash.altafi.mvisample.data.dataSource.UsersPagingSource
import com.arash.altafi.mvisample.data.model.UserResponse
import com.arash.altafi.mvisample.utils.base.BaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PagingRepository @Inject constructor(
    private val service: PagingService,
) : BaseRepository() {

    fun getUsersPagingData(pageSize: Int): Flow<PagingData<UserResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UsersPagingSource(service, pageSize) }
        ).flow
    }

}