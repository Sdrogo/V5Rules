package com.example.v5rules.data

import android.content.Context
import com.example.v5rules.utils.ChapterReader
import com.example.v5rules.utils.DisciplineReader
import java.util.Locale

class MainRepository(context: Context) {

    private val disciplineReader = DisciplineReader(context)
    private val chapterReader = ChapterReader(context)
    fun loadDisciplines(language: Locale): List<Discipline> {
        return disciplineReader.readDisciplines(language)
    }
    fun loadChapters(language: Locale): List<Chapter> {
        return chapterReader.readChapters(language)
    }
}
