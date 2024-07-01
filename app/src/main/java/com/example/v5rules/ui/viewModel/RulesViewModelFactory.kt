package com.example.v5rules.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.v5rules.data.MainRepository

class RulesViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RulesViewModel::class.java)) {
            val mainRepository = MainRepository(context)
            @Suppress("UNCHECKED_CAST")
            return RulesViewModel(mainRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
