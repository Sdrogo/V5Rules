package com.example.v5rules.data.local.repository

import com.example.v5rules.data.local.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getAllCharacters(): Flow<List<Character>>
    suspend fun getCharacter(id: String): Character?
    suspend fun saveCharacter(character: Character): String // Ritorna l'ID del personaggio (nuovo o esistente)
    suspend fun deleteCharacter(character: Character)
}