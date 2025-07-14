package com.example.v5rules.ui.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.v5rules.R
import com.example.v5rules.data.Gender

@Composable
fun GenderSelection(
    selectedGender: Gender,
    onGenderSelected: (Gender) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selectedGender == Gender.MALE,
                onClick = { onGenderSelected(Gender.MALE) }
            )
            Text(
                text = stringResource(id = R.string.male),
                color = MaterialTheme.colorScheme.primary
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selectedGender == Gender.FEMALE,
                onClick = { onGenderSelected(Gender.FEMALE) }
            )
            Text(
                text = stringResource(id = R.string.female),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
