package com.arash.altafi.mvisample.data.repository

import com.arash.altafi.mvisample.data.api.UserService
import com.arash.altafi.mvisample.utils.base.BaseRepository
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val service: UserService,
) : BaseRepository() {

    fun getUsers(pageNumber: Int, pageSize: Int) = callApi {
        service.getUsersPaging(pageNumber, pageSize)
    }

}

