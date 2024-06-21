package com.example.v5rules.utils

import android.content.Context
import com.example.v5rules.R
import com.example.v5rules.data.Discipline
import com.google.gson.Gson
import java.io.InputStreamReader
import java.util.Locale

class DisciplineReader(private val context: Context) {

    fun readDisciplines(language: Locale): List<Discipline> {
        val resourceId = when (language) {
            Locale.ENGLISH -> R.raw.discipline_it
            Locale.ITALIAN -> R.raw.discipline_it
            else -> {R.raw.discipline_it}
        }

        val inputStream = context.resources.openRawResource(resourceId)
        val reader = InputStreamReader(inputStream)
        return Gson().fromJson(reader, Array<Discipline>::class.java).toList()
    }
}
