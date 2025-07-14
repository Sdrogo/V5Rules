package com.example.v5rules.repository

import com.example.v5rules.data.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    suspend fun getCharacter(id: Int): Character? // Ottieni una scheda per ID
    fun getAllCharacters(): Flow<List<Character>> // Ottieni tutte le schede
    suspend fun saveCharacter(character: Character) : Int// Salva una scheda E RESTITUISCE L'ID
    suspend fun deleteCharacter(character: Character) // Elimina una scheda
}