package com.example.v5rules.ui.compose.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.v5rules.data.Paragraph

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParagraphScreen(paragraph: Paragraph, navController: NavController) {
    Scaffold(
        topBar = {
            // Mostra il titolo del paragrafo e un pulsante per tornare alla schermata padre
            TopAppBar(
                title = { Text(text = paragraph.title) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        // Mostra il contenuto del paragrafo
        Text(
            text = paragraph.content,
            modifier = Modifier.padding(innerPadding)
        )
    }
}