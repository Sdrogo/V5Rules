package com.example.v5rules.data

import android.content.Context
import com.example.v5rules.utils.ClanReader
import com.example.v5rules.utils.DisciplineReader
import com.example.v5rules.utils.LoresheetReader
import com.example.v5rules.utils.PredatorTypeReader
import com.example.v5rules.utils.RulesReader
import java.util.Locale

class MainRepository(context: Context) {

    private val disciplineReader = DisciplineReader(context)
    private val clanReader = ClanReader(context)
    private val predatorTypeReader = PredatorTypeReader(context)
    private val rulesReader = RulesReader(context)
    private val loresheetReader = LoresheetReader(context)

    fun loadDisciplines(language: Locale): List<Discipline> {
        return disciplineReader.readDisciplines(language)
    }
    fun loadClans(language: Locale): List<Clan> {
        return clanReader.readClans(language)
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

}
