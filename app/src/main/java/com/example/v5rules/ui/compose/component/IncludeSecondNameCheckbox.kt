package com.example.v5rules.ui.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.v5rules.R

@Composable
fun IncludeSecondNameCheckbox(
    includeSecondName: Boolean,
    onIncludeSecondNameChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Checkbox(
            checked = includeSecondName,
            onCheckedChange = { onIncludeSecondNameChange(it) },
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = stringResource(id = R.string.include_second_name))
    }
}
