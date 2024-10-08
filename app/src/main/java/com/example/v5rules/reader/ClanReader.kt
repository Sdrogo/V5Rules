package com.example.v5rules.reader

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
                R.raw.clan_it
            }
        }
        val inputStream = context.resources.openRawResource(resourceId)
        val reader = InputStreamReader(inputStream)
        return Gson().fromJson(reader, Array<Clan>::class.java).toList()
    }


    enum class ClanImage(
        val clanName: String,
        val logoImageResource: Int,
        val nameImageResource: Int
    ) {
        BANUHAQIM("Banu Haqim", R.drawable.banu_haqim_symbol, R.drawable.banu_haqim_logo),
        BRUJAH("Brujah", R.drawable.brujah_symbol, R.drawable.brujah_logo),
        GANGREL("Gangrel", R.drawable.gangrel_symbol, R.drawable.gangrel_logo),
        HECATA("Hecata", R.drawable.hecata_symbol, R.drawable.hecata_logo),
        LASOMBRA("Lasombra", R.drawable.lasombra_symbol, R.drawable.lasombra_logo),
        MALKAVIAN("Malkavian", R.drawable.malkavian_symbol, R.drawable.malkavian_logo),
        MINISTRY("Il Ministero", R.drawable.ministry_symbol, R.drawable.ministry_logo),
        NOSFERATU("Nosferatu", R.drawable.nosferatu_symbol, R.drawable.nosferatu_logo),
        RAVNOS("Ravnos", R.drawable.ravnos_symbol, R.drawable.ravnos_logo),
        SALUBRI("Salubri", R.drawable.salubri_symbol, R.drawable.salubri_logo),
        TOREADOR("Toreador", R.drawable.toreador_symbol, R.drawable.toreador_logo),
        TREMERE("Tremere", R.drawable.tremere_symbol, R.drawable.tremere_logo),
        TZIMISCE("Tzimisce", R.drawable.tzimisce_symbol, R.drawable.tzimisce_logo),
        VENTRUE("Ventrue", R.drawable.ventrue_symbol, R.drawable.ventrue_logo),
        CAITIFF("I Vili", R.drawable.caitiff_symbol, R.drawable.caitiff_logo),
        THINBLOOD("Sangue Debole", R.drawable.thinblood_symbol, R.drawable.thinblood_logo)
    }
}
