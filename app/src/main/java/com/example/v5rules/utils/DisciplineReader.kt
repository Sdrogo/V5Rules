package com.example.v5rules.utils

import android.content.Context
import com.example.v5rules.R
import com.example.v5rules.data.Discipline
import com.google.gson.Gson
import java.io.InputStreamReader

class DisciplineReader(private val context: Context) {

    fun readDisciplines(language: Language): List<Discipline> {
        val resourceId = when (language) {
            Language.ENGLISH -> R.raw.rules_en
            Language.ITALIAN -> R.raw.rules_it
        }

        val inputStream = context.resources.openRawResource(resourceId)
        val reader = InputStreamReader(inputStream)
        return Gson().fromJson(reader, Array<Discipline>::class.java).toList()
    }
}
