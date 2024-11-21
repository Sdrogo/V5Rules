package com.example.v5rules.reader

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
    fun readLore(language: Locale): List<Chapter> {
        val resourceId = when (language) {
            Locale.ITALIAN -> R.raw.lore_it
            else -> {R.raw.lore_it}
        }

        val inputStream = context.resources.openRawResource(resourceId)
        val reader = InputStreamReader(inputStream)
        return Gson().fromJson(reader, Array<Chapter>::class.java).toList()
    }
    fun readKindred(language: Locale): List<Chapter> {
        val resourceId = when (language) {
            Locale.ITALIAN -> R.raw.kindred_it
            else -> {R.raw.kindred_it}
        }
        val inputStream = context.resources.openRawResource(resourceId)
        val reader = InputStreamReader(inputStream)
        return Gson().fromJson(reader, Array<Chapter>::class.java).toList()
    }
    fun readPg(language: Locale): List<Chapter> {
        val resourceId = when (language) {
            Locale.ITALIAN -> R.raw.pg_it
            else -> {R.raw.pg_it}
        }
        val inputStream = context.resources.openRawResource(resourceId)
        val reader = InputStreamReader(inputStream)
        return Gson().fromJson(reader, Array<Chapter>::class.java).toList()
    }
}
