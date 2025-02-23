package com.arash.altafi.mvisample.data.db

import androidx.room.*
import com.arash.altafi.mvisample.data.model.FavoriteUserEntity

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(user: FavoriteUserEntity)

    @Delete
    suspend fun removeFavorite(user: FavoriteUserEntity)

    @Query("SELECT * FROM favorite_users WHERE id = :userId")
    suspend fun getFavoriteById(userId: Int): FavoriteUserEntity?

    @Query("SELECT * FROM favorite_users")
    suspend fun getAllFavorites(): List<FavoriteUserEntity>
}