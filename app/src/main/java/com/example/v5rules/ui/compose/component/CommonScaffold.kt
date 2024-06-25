package com.example.v5rules.ui.compose.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.R
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonScaffold(
    navController: NavHostController,
    title: String,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar( // Use CenterAlignedTopAppBar for centered title
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center // Center the text
                    )
                },
                navigationIcon = {
                    if (title != stringResource(id = R.string.app_name)) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Go Back",
                                tint = Color.White
                            )
                        }
                    }
                },
                modifier = Modifier.padding(bottom = 16.dp),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors( // Use centerAlignedTopAppBarColors
                    containerColor = Color(0xFF76031a), // Set the container color to red
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}