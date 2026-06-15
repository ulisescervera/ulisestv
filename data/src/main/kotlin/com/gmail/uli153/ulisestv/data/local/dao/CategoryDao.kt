package com.gmail.uli153.ulisestv.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.gmail.uli153.ulisestv.data.dto.CategoryDto
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAll(): Flow<List<CategoryDto>>

    @Query("SELECT * FROM categories WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun search(query: String): Flow<List<CategoryDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(categories: List<CategoryDto>)

    @Query("DELETE FROM categories")
    suspend fun clear()

    /** Atomically replaces the whole table with [categories]. */
    @Transaction
    suspend fun replaceAll(categories: List<CategoryDto>) {
        clear()
        upsertAll(categories)
    }
}
