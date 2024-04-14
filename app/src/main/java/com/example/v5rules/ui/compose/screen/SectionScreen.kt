package com.example.v5rules.ui.compose.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.v5rules.data.Section
import com.example.v5rules.ui.compose.component.ParagraphItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionScreen(section: Section, navController: NavController) {
    Scaffold(
        topBar = {
            // Mostra il titolo della sezione e un pulsante per tornare alla schermata padre
            TopAppBar(
                title = { Text(text = section.title) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            itemsIndexed(section.paragraphs) { index, paragraph ->
                ParagraphItem(paragraph = paragraph)
            }
        }
    }
}