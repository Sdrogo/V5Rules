package com.example.v5rules.ui.compose.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ContentExpander(title: String,  initialState:Boolean = false, content: @Composable () -> Unit) {
    var expandedDescription by remember { mutableStateOf(initialState) }
    Column(modifier = Modifier.clickable{ expandedDescription = !expandedDescription }){
        Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, modifier = Modifier.clickable{ expandedDescription = !expandedDescription})
        AnimatedVisibility(visible = expandedDescription){
            content()
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}