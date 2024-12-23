package com.example.v5rules.ui.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.v5rules.data.Npc

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GeneratedName(
    npc: Npc?,
    widthFloat: Float = 1f
) {
        FlowRow (
            modifier = Modifier
                .fillMaxWidth(widthFloat)
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.Center,
            maxItemsInEachRow = 1){
            npc?.nome?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            npc?.secondName?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            npc?.cognome?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
}