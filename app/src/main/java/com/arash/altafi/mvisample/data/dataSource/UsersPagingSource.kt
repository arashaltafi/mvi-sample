package com.arash.altafi.mvisample.data.dataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arash.altafi.mvisample.data.api.PagingService
import com.arash.altafi.mvisample.data.model.UserResponse
import retrofit2.HttpException
import java.io.IOException

class UsersPagingSource(
    private val service: PagingService,
    private val pageSize: Int
) : PagingSource<Int, UserResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserResponse> {
        val pageNumber = params.key ?: 1
        return try {
            val response = service.getUsers(pageNumber, pageSize)
            if (response.isSuccessful) {
                val users = response.body() ?: emptyList()
                // If the returned list is smaller than pageSize, assume it's the last page
                val nextKey = if (users.size < pageSize) null else pageNumber + 1
                LoadResult.Page(
                    data = users,
                    prevKey = if (pageNumber == 1) null else pageNumber - 1,
                    nextKey = nextKey
                )
            } else {
                LoadResult.Error(Exception("Error code: ${response.code()}"))
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UserResponse>): Int? {
        return state.anchorPosition?.let { position ->
            val page = state.closestPageToPosition(position)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }
}
