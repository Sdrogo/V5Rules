package com.example.v5rules.ui.compose.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NationalityDropdown(
    nationalities: List<String>,
    onNationalitySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedNationality by remember { mutableStateOf(nationalities.firstOrNull()) }

    Box(
        modifier = Modifier.wrapContentSize()
    ) {
        TextButton(
            onClick = { expanded = true },
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(selectedNationality ?: "Select nationality")
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            nationalities.forEach { nationality ->
                DropdownMenuItem(
                    onClick = {
                        selectedNationality = nationality
                        expanded = false
                        onNationalitySelected(nationality)
                    },
                    text = {Text(nationality)}
                )
            }
        }
    }
}