package com.example.v5rules.repository

import com.example.v5rules.data.FavoriteNpc
import kotlinx.coroutines.flow.Flow

interface FavoriteNpcRepository {
    fun getAllFavorites(): Flow<List<FavoriteNpc>>
    suspend fun addFavorite(npc: FavoriteNpc)
    suspend fun removeFavorite(npc: FavoriteNpc)
    suspend fun findFavorite(name: String, familyName: String, secondName: String?): FavoriteNpc?
}
