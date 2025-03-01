package com.arash.altafi.mvisample.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.arash.altafi.mvisample.data.dataSource.TestDataSource
import com.arash.altafi.mvisample.data.dataSource.TestPagingSource
import com.arash.altafi.mvisample.data.db.TestDetailDao
import com.arash.altafi.mvisample.data.model.TestDetailEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestRepository @Inject constructor(
    private val remoteDataSource: TestDataSource,
    private val userDao: TestDetailDao
) {
    // API 1: Non-paged list
    suspend fun getUserList(): List<TestDetailEntity>? = remoteDataSource.fetchUserList()

    // API 2: Paging list using Paging3
    fun getUserPaging(pageSize: Int): Flow<PagingData<TestDetailEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { TestPagingSource(remoteDataSource, pageSize) }
        ).flow
    }

    // API 3: Detail – first check Room cache; if missing, call API and then save
    suspend fun getUserDetail(id: String): TestDetailEntity? {
        return userDao.getTestById(id) ?: remoteDataSource.fetchUserDetail(id)
            ?.also { userDao.insertTest(it) }
    }
}
