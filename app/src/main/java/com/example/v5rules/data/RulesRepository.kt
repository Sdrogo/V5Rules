package com.example.v5rules.data

import android.content.Context
import com.example.v5rules.utils.DisciplineReader
import com.example.v5rules.utils.Language

class RulesRepository(private val context: Context) {

    private val disciplineReader = DisciplineReader(context)

    fun loadRules(language: Language): List<Discipline> {
        return disciplineReader.readDisciplines(language)
    }
}
