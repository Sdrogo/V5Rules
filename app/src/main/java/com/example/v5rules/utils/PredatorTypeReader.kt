package com.example.v5rules.utils

import android.content.Context
import com.example.v5rules.R
import com.example.v5rules.data.PredatorType
import com.google.gson.Gson
import java.io.InputStreamReader
import java.util.Locale

class PredatorTypeReader(private val context: Context) {

    fun readPredatorType(language: Locale): List<PredatorType> {
        val resourceId = when (language) {
            Locale.ENGLISH -> R.raw.predator_it
            Locale.ITALIAN -> R.raw.predator_it
            else -> {
                R.raw.predator_it}
        }
        val inputStream = context.resources.openRawResource(resourceId)
        val reader = InputStreamReader(inputStream)
        return Gson().fromJson(reader, Array<PredatorType>::class.java).toList()
    }
}
