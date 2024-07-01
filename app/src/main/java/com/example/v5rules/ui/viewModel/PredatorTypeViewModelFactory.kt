package com.example.v5rules.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.v5rules.data.MainRepository

class PredatorTypeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PredatorTypeViewModel::class.java)) {
            val mainRepository = MainRepository(context)
            @Suppress("UNCHECKED_CAST")
            return PredatorTypeViewModel(mainRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
