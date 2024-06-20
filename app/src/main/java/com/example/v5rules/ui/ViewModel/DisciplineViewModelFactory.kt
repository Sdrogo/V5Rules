package com.example.v5rules.ui.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.v5rules.data.RulesRepository

class DisciplineViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DisciplineViewModel::class.java)) {
            val rulesRepository = RulesRepository(context)
            @Suppress("UNCHECKED_CAST")
            return DisciplineViewModel(rulesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
