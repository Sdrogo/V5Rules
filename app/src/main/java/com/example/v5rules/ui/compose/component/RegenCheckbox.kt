package com.example.v5rules.ui.compose.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun RegenerationCheckbox(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    width:Float = 0.5f,
    enabled: Boolean = true, // Default to true
    firstGeneration: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(width),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled && firstGeneration // Enable only if firstGeneration is true
        )
        Text(text = text, color = MaterialTheme.colorScheme.primary)
    }
}