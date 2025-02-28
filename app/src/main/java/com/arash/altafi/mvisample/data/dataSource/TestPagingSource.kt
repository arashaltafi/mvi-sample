package com.arash.altafi.mvisample.data.dataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arash.altafi.mvisample.data.model.TestDetailEntity
import retrofit2.HttpException
import java.io.IOException

class TestPagingSource(
    private val remoteDataSource: TestDataSource,
    private val pageSize: Int
) : PagingSource<Int, TestDetailEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TestDetailEntity> {
        val page = params.key ?: 1
        return try {
            val users = remoteDataSource.fetchUserPaging(page, pageSize) ?: emptyList()
            val nextKey = if (users.size < pageSize) null else page + 1
            LoadResult.Page(
                data = users,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TestDetailEntity>): Int? =
        state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
}