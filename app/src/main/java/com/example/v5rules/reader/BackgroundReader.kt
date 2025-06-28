package com.example.v5rules.reader

import android.content.Context
import com.example.v5rules.R
import com.example.v5rules.data.Background
import com.google.gson.Gson
import java.io.InputStreamReader
import java.util.Locale

class BackgroundReader(private val context: Context) {

    fun readBackground(language: Locale): List<Background> {
        val resourceId = when (language) {
            Locale.ITALIAN -> R.raw.background_it
            else -> {R.raw.background_it}
        }

        val inputStream = context.resources.openRawResource(resourceId)
        val reader = InputStreamReader(inputStream)
        return Gson().fromJson(reader, Array<Background>::class.java).toList()
    }

}
