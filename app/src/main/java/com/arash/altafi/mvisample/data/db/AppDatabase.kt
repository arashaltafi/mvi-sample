package com.arash.altafi.mvisample.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arash.altafi.mvisample.data.model.TestDetailEntity

@Database(entities = [TestDetailEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun testDetailDao(): TestDetailDao
}