package com.example.v5rules.data

import android.content.Context
import com.example.v5rules.utils.ChapterReader
import com.example.v5rules.utils.ClanReader
import com.example.v5rules.utils.DisciplineReader
import com.example.v5rules.utils.PredatorTypeReader
import java.util.Locale

class MainRepository(context: Context) {

    private val disciplineReader = DisciplineReader(context)
    private val chapterReader = ChapterReader(context)
    private val clanReader = ClanReader(context)
    private val predatorTypeReader = PredatorTypeReader(context)

    fun loadDisciplines(language: Locale): List<Discipline> {
        return disciplineReader.readDisciplines(language)
    }
    fun loadChapters(language: Locale): List<Chapter> {
        return chapterReader.readChapters(language)
    }
    fun loadClans(language: Locale): List<Clan> {
        return clanReader.readClans(language)
    }

    fun loadPredatorType(language: Locale): List<PredatorType> {
        return predatorTypeReader.readPredatorType(language)
    }

}
