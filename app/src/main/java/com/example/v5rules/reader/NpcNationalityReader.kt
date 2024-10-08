package com.example.v5rules.reader

import android.annotation.SuppressLint
import android.content.Context
import com.example.v5rules.R
import com.example.v5rules.data.NationalityNpc
import com.google.gson.Gson
import java.io.InputStreamReader
import java.util.Locale

@SuppressLint("DiscouragedApi")
class NpcReader(private val context: Context) {
    fun readNpc(language: Locale): List<NationalityNpc> {
        val resourceId = when (language) {
            Locale.ENGLISH -> R.raw.name_list
            Locale.ITALIAN -> R.raw.name_list
            else -> {
                R.raw.name_list
            }
        }
        val inputStream = context.resources.openRawResource(resourceId)
        val reader = InputStreamReader(inputStream)
        return Gson().fromJson(reader, Array<NationalityNpc>::class.java).toList()
    }
}
