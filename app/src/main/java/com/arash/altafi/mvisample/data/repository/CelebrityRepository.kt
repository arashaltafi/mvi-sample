package com.arash.altafi.mvisample.data.repository

import com.arash.altafi.mvisample.data.api.CelebrityService
import com.arash.altafi.mvisample.utils.base.BaseRepository
import javax.inject.Inject

class CelebrityRepository @Inject constructor(
    private val service: CelebrityService,
) : BaseRepository() {

    fun getCelebrities(pageNumber: Int, pageSize: Int) = callApi {
        service.getCelebrities(pageNumber, pageSize)
    }

}