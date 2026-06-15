package com.gmail.uli153.ulisestv.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.gmail.uli153.ulisestv.data.dto.VideoDto
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {

    @Query("SELECT * FROM videos ORDER BY name ASC")
    fun getAll(): Flow<List<VideoDto>>

    @Query("SELECT * FROM videos WHERE category_id = :categoryId ORDER BY name ASC")
    fun getByCategory(categoryId: Int): Flow<List<VideoDto>>

    @Query("SELECT * FROM videos WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun search(query: String): Flow<List<VideoDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(videos: List<VideoDto>)

    @Query("DELETE FROM videos")
    suspend fun clear()

    /** Atomically replaces the whole table with [videos]. */
    @Transaction
    suspend fun replaceAll(videos: List<VideoDto>) {
        clear()
        upsertAll(videos)
    }
}
