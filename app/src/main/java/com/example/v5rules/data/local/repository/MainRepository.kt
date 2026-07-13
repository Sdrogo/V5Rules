package com.example.v5rules.data.local.repository

import android.content.Context
import com.example.v5rules.data.local.model.Background
import com.example.v5rules.data.local.model.Chapter
import com.example.v5rules.data.local.model.Character
import com.example.v5rules.data.local.model.Clan
import com.example.v5rules.data.local.model.Discipline
import com.example.v5rules.data.local.model.Loresheet
import com.example.v5rules.data.local.model.NationalityNpc
import com.example.v5rules.data.local.model.PredatorType
import com.example.v5rules.data.local.reader.BackgroundReader
import com.example.v5rules.data.local.reader.ClanReader
import com.example.v5rules.data.local.reader.DisciplineReader
import com.example.v5rules.data.local.reader.LoresheetReader
import com.example.v5rules.data.local.reader.NpcReader
import com.example.v5rules.data.local.reader.PredatorTypeReader
import com.example.v5rules.data.local.reader.RulesReader
import kotlinx.coroutines.flow.Flow
import java.util.Locale

class MainRepository(context: Context) : CharacterRepository {

    private val disciplineReader = DisciplineReader(context)
    private val clanReader = ClanReader(context)
    private val predatorTypeReader = PredatorTypeReader(context)
    private val rulesReader = RulesReader(context)
    private val loresheetReader = LoresheetReader(context)
    private val npcReader = NpcReader(context)
    private val backgroundReader = BackgroundReader(context)

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
    fun loadBackground(lenguage: Locale): List<Background> {
        return backgroundReader.readBackground(lenguage)
    }
    fun loadKindred(language: Locale): List<Chapter> {
        return rulesReader.readKindred(language)
    }
    fun loadPg(language: Locale): List<Chapter> {
        return rulesReader.readPg(language)
    }

    override suspend fun getCharacter(id: String): Character {
        throw UnsupportedOperationException("Not implemented in MainRepository")
    }

    override fun getAllCharacters(): Flow<List<Character>> {
        throw UnsupportedOperationException("Not implemented in MainRepository")
    }

    override suspend fun saveCharacter(character: Character) : String {
        throw UnsupportedOperationException("Not implemented in MainRepository")
    }

    override suspend fun deleteCharacter(character: Character) {
        throw UnsupportedOperationException("Not implemented in MainRepository")
    }

}