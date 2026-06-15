package com.gmail.uli153.ulisestv.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gmail.uli153.ulisestv.data.dto.CategoryDto
import com.gmail.uli153.ulisestv.data.dto.VideoDto
import com.gmail.uli153.ulisestv.data.local.dao.CategoryDao
import com.gmail.uli153.ulisestv.data.local.dao.VideoDao

@Database(
    entities = [CategoryDto::class, VideoDto::class],
    version = 1,
    exportSchema = false,
)
abstract class UlisesTvDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun videoDao(): VideoDao
}
