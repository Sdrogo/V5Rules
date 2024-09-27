package com.example.v5rules.utils

import android.content.Context
import com.example.v5rules.R
import com.example.v5rules.data.Chapter
import com.google.gson.Gson
import java.io.InputStreamReader
import java.util.Locale

class RulesReader(private val context: Context) {

    fun readRules(language: Locale): List<Chapter> {
        val resourceId = when (language) {
            Locale.ITALIAN -> R.raw.rules_it
            else -> {R.raw.rules_it}
        }

        val inputStream = context.resources.openRawResource(resourceId)
        val reader = InputStreamReader(inputStream)
        return Gson().fromJson(reader, Array<Chapter>::class.java).toList()
    }
}
