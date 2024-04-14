package com.example.v5rules.ui.compose.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.v5rules.R

@SuppressLint("SuspiciousIndentation")
@Composable
fun IndexScreen(innerPadding:PaddingValues) {
    val lazyListState = rememberLazyListState() // Dichiarazione dello stato di scorrimento

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            state = lazyListState // Applicazione dello stato di scorrimento
        ) {
            // Aggiungi un'intestazione come elemento separato prima degli elementi principali
            item {
                // Intestazione
                Text("Intestazione")
            }

            // Visualizza le regole di Vampiri utilizzando LazyColumn
            items(100) { index ->
                Text("Regola $index")
            }
        }
}
