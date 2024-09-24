package com.example.v5rules.ui.compose.component

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
    isListEmpty : Boolean,
    onGenerateNPC: () -> Unit
) {
    Button(
        enabled = !selectedNationality.isNullOrEmpty() || !isListEmpty,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary, // Your desired button color
            contentColor = MaterialTheme.colorScheme.primary // Text color for contrast
        ),
        onClick = {
            selectedNationality?.let {
                onGenerateNPC()
            }
        },
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(
            text = if (!firstGeneration) {
                stringResource(id = R.string.generate_button_label)
            } else {
                stringResource(id = R.string.regenerate_button_label)
            }
        )
    }
}