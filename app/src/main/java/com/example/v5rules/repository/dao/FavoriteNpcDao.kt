package com.example.v5rules.repository.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.v5rules.data.FavoriteNpc
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteNpcDao {

    @Query("SELECT * FROM favorite_npcs ORDER BY name ASC")
    fun getAllFavorites(): Flow<List<FavoriteNpc>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoriteNpc: FavoriteNpc)

    @Delete
    suspend fun delete(favoriteNpc: FavoriteNpc)

    @Query("SELECT * FROM favorite_npcs WHERE name = :name AND familyName = :familyName AND (:secondName IS NULL OR secondName = :secondName) LIMIT 1")
    suspend fun findFavorite(name: String, familyName: String, secondName: String?): FavoriteNpc?
}