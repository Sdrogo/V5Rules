package com.example.v5rules.utils

import android.annotation.SuppressLint
import android.content.res.Resources
import com.example.v5rules.R
import org.json.JSONArray
import org.json.JSONObject

@SuppressLint("DiscouragedApi")
fun JSONReader(resources: Resources, nazionalita: String): Map<String, List<String>> {
    val resourceId = resources.getIdentifier("name_list", "raw", resources.getResourcePackageName(R.raw.name_list))
    val jsonFile = resources.openRawResource(resourceId)
    val jsonText = jsonFile.bufferedReader().use { it.readText() }
    val nomiCognomiMap = mutableMapOf<String, List<String>>()
    val jsonObject = JSONArray(jsonText)

    var nazionalitaObj: JSONObject? = null
    for (i in 0 until jsonObject.length()) {
        val obj = jsonObject.getJSONObject(i)
        if (obj.has(nazionalita)) {
            nazionalitaObj = obj.getJSONObject(nazionalita)
            break
        }
    }

    if (nazionalitaObj != null) {
        val nomiMaschiliArray = nazionalitaObj.getJSONArray("nomi_maschili")
        val nomiFemminiliArray = nazionalitaObj.getJSONArray("nomi_femminili")
        val cognomiArray = nazionalitaObj.getJSONArray("cognomi")

        val nomiMaschiliList = mutableListOf<String>()
        for (i in 0 until nomiMaschiliArray.length()) {
            nomiMaschiliList.add(nomiMaschiliArray.getString(i))
        }
        val nomiFemminiliList = mutableListOf<String>()
        for (i in 0 until nomiFemminiliArray.length()) {
            nomiFemminiliList.add(nomiFemminiliArray.getString(i))
        }
        val cognomiList = mutableListOf<String>()
        for (i in 0 until cognomiArray.length()) {
            cognomiList.add(cognomiArray.getString(i))
        }

        nomiCognomiMap["nomi_maschili"] = nomiMaschiliList
        nomiCognomiMap["nomi_femminili"] = nomiFemminiliList
        nomiCognomiMap["cognomi"] = cognomiList
    }

    return nomiCognomiMap
}