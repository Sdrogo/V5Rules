package com.example.v5rules.ui.compose.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.v5rules.R


@Composable
fun GenerateButton(
    selectedNationality: String?,
    firstGeneration: Boolean,
    width: Float = 1f,
    isListEmpty : Boolean,
    onGenerateNPC: () -> Unit
) {
    Button(
        enabled = !selectedNationality.isNullOrEmpty() || !isListEmpty,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary, // Your desired button color
            contentColor = MaterialTheme.colorScheme.secondary // Text color for contrast
        ),
        onClick = {
            selectedNationality?.let {
                onGenerateNPC()
            }
        },
        modifier = Modifier.fillMaxWidth(width).padding(8.dp)
    ) {
        Text(color = MaterialTheme.colorScheme.secondary,
            text = if (!firstGeneration) {
                stringResource(id = R.string.generate_button_label)
            } else {
                stringResource(id = R.string.regenerate_button_label)
            }
        )
    }
}