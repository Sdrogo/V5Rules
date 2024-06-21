package com.example.v5rules.data

import android.content.Context
import com.example.v5rules.utils.DisciplineReader
import com.example.v5rules.utils.Language
import java.util.Locale

class DisciplineRepository(context: Context) {

    private val disciplineReader = DisciplineReader(context)

    fun loadDiscipline(language: Locale): List<Discipline> {
        return disciplineReader.readDisciplines(language)
    }
}
