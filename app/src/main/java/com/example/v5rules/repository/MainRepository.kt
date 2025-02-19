package com.example.v5rules.repository

import android.content.Context
import com.example.v5rules.data.Chapter
import com.example.v5rules.data.Character
import com.example.v5rules.data.Clan
import com.example.v5rules.data.Discipline
import com.example.v5rules.data.Loresheet
import com.example.v5rules.data.NationalityNpc
import com.example.v5rules.data.PredatorType
import com.example.v5rules.reader.ClanReader
import com.example.v5rules.reader.DisciplineReader
import com.example.v5rules.reader.LoresheetReader
import com.example.v5rules.reader.NpcReader
import com.example.v5rules.reader.PredatorTypeReader
import com.example.v5rules.reader.RulesReader
import java.util.Locale

class MainRepository(context: Context) : CharacterRepository {

    private val disciplineReader = DisciplineReader(context)
    private val clanReader = ClanReader(context)
    private val predatorTypeReader = PredatorTypeReader(context)
    private val rulesReader = RulesReader(context)
    private val loresheetReader = LoresheetReader(context)
    private val npcReader = NpcReader(context)

    fun loadDisciplines(language: Locale): List<Discipline> {
        return disciplineReader.readDisciplines(language)
    }
    fun loadClans(language: Locale): List<Clan> {
        return clanReader.readClans(language)
    }

    fun readNpcNames(language: Locale): List<NationalityNpc> {
        return npcReader.readNpc(language)
    }
    fun loadPredatorType(language: Locale): List<PredatorType> {
        return predatorTypeReader.readPredatorType(language)
    }

    fun loadRules(language: Locale): List<Chapter> {
        return rulesReader.readRules(language)
    }

    fun loadLore(language: Locale): List<Chapter> {
        return rulesReader.readLore(language)
    }
    fun loadLoresheet(lenguage: Locale): List<Loresheet> {
        return loresheetReader.readLoresheets(lenguage)
    }
    fun loadKindred(language: Locale): List<Chapter> {
        return rulesReader.readKindred(language)
    }
    fun loadPg(language: Locale): List<Chapter> {
        return rulesReader.readPg(language)
    }

    override suspend fun getCharacter(id: Int): Character? {
        throw UnsupportedOperationException("Not implemented in MainRepository")
    }

    override suspend fun getAllCharacters(): List<Character> {
        throw UnsupportedOperationException("Not implemented in MainRepository")
    }

    override suspend fun saveCharacter(character: Character) {
        throw UnsupportedOperationException("Not implemented in MainRepository")
    }

    override suspend fun deleteCharacter(character: Character) {
        throw UnsupportedOperationException("Not implemented in MainRepository")
    }

}
