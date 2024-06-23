package com.example.v5rules.utils

import android.content.Context
import com.example.v5rules.R
import com.example.v5rules.data.Chapter
import com.google.gson.Gson
import java.io.InputStreamReader
import java.util.Locale

class ChapterReader(private val context: Context) {

    fun readChapters(language: Locale): List<Chapter> {
        val resourceId = when (language) {
            Locale.ENGLISH -> R.raw.rules_en
            Locale.ITALIAN -> R.raw.rules_it
            else -> {R.raw.rules_it}
        }

        val inputStream = context.resources.openRawResource(resourceId)
        val reader = InputStreamReader(inputStream)
        return Gson().fromJson(reader, Array<Chapter>::class.java).toList()
    }
}
