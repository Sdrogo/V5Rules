package com.example.v5rules.repository

import com.example.v5rules.data.FavoriteNpc
import com.example.v5rules.repository.dao.FavoriteNpcDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteNpcRepositoryImpl @Inject constructor(
    private val favoriteNpcDao: FavoriteNpcDao
) : FavoriteNpcRepository {

    override fun getAllFavorites(): Flow<List<FavoriteNpc>> = favoriteNpcDao.getAllFavorites()

    override suspend fun addFavorite(npc: FavoriteNpc) {
        favoriteNpcDao.insert(npc)
    }

    override suspend fun removeFavorite(npc: FavoriteNpc) {
        // Troviamo il preferito nel DB per avere il suo ID corretto prima di cancellare
        val favoriteToDelete = favoriteNpcDao.findFavorite(npc.name, npc.familyName, npc.secondName)
        if (favoriteToDelete != null) {
            favoriteNpcDao.delete(favoriteToDelete)
        }
    }

    override suspend fun findFavorite(name: String, familyName: String, secondName: String?): FavoriteNpc? {
        return favoriteNpcDao.findFavorite(name, familyName, secondName)
    }
}
