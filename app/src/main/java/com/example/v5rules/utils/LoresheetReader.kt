package com.example.v5rules.utils

import android.content.Context
import com.example.v5rules.R
import com.example.v5rules.data.Chapter
import com.example.v5rules.data.Loresheet
import com.google.gson.Gson
import java.io.InputStreamReader
import java.util.Locale

class LoresheetReader(private val context: Context) {

    fun readLoresheets(language: Locale): List<Loresheet> {
        val resourceId = when (language) {
            Locale.ITALIAN -> R.raw.loresheet_it
            else -> {R.raw.loresheet_it}
        }

        val inputStream = context.resources.openRawResource(resourceId)
        val reader = InputStreamReader(inputStream)
        return Gson().fromJson(reader, Array<Loresheet>::class.java).toList()
    }

}
