package com.example.v5rules.utils

import android.content.Context
import com.example.v5rules.R
import com.example.v5rules.data.Clan
import com.google.gson.Gson
import java.io.InputStreamReader
import java.util.Locale

class ClanReader(private val context: Context) {

    fun readClans(language: Locale): List<Clan> {
        val resourceId = when (language) {
            Locale.ENGLISH -> R.raw.clan_it
            Locale.ITALIAN -> R.raw.clan_it
            else -> {
                R.raw.clan_it}
        }
        val inputStream = context.resources.openRawResource(resourceId)
        val reader = InputStreamReader(inputStream)
        return Gson().fromJson(reader, Array<Clan>::class.java).toList()
    }
}
