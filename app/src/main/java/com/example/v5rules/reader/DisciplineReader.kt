package com.example.v5rules.reader

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

    enum class DisciplineImage(val disciplineId: String, val imageResource: Int) {
        ANIMALISM("d0", R.drawable.animalism_symbol),
        AUSPEX("d1", R.drawable.auspex_symbol),
        DOMINATE("d2", R.drawable.dominate_symbol),
        PRESENCE("d3", R.drawable.presence_symbol),
        OBFUSCATE("d4", R.drawable.obfuscate_symbol),
        POTENCE("d5", R.drawable.potence_symbol),
        PROTEAN("d6", R.drawable.protean_symbol),
        FORTITUDE("d7", R.drawable.fortitude_symbol),
        CELERITY("d8", R.drawable.celerity_symbol),
        BLOODSORCERY("d9", R.drawable.blood_sorcery_symbol),
        OBLIVION("d10", R.drawable.oblivion_symbol),
        THINBLOODALCHEMY("d11", R.drawable.alchemy_symbol)


        // ... add other disciplines here
    }

}
