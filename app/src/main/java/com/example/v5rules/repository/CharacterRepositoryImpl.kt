package com.example.v5rules.repository

import com.example.v5rules.repository.dao.CharacterDao
import javax.inject.Inject
import javax.inject.Singleton
import com.example.v5rules.data.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class CharacterRepositoryImpl @Inject constructor(
    private val characterDao: CharacterDao, // Inietta il DAO
    private val mainRepository: MainRepository? = null // Se hai bisogno di MainRepository. Metti ? = null se non lo usi.
): CharacterRepository {

    override fun getAllCharacters(): Flow<List<Character>> =
        characterDao.getAllCharacters().map { list ->
            list.map { it }  // Converto in Character
        }

    override suspend fun getCharacter(id: Int): Character? {
        return characterDao.getCharacterById(id)
    }

    override suspend fun saveCharacter(character: Character) {
        if (character.id == 0) {
            characterDao.insertCharacter(character)
        } else {
            characterDao.updateCharacter(character)
        }
    }

    override suspend fun deleteCharacter(character: Character) {
        characterDao.deleteCharacter(character)
    }
}