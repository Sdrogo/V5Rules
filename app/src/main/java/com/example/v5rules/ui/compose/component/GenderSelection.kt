package com.example.v5rules.ui.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.v5rules.data.Gender
import com.example.v5rules.R

@Composable
fun GenderSelection(
    selectedGender: Gender,
    onGenderSelected: (Gender) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(stringResource(id = R.string.gender_label))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            )  {
                RadioButton(
                    selected = selectedGender == Gender.MALE,
                    onClick = { onGenderSelected(Gender.MALE) },
                    enabled = selectedGender != Gender.MALE
                )
                Text(stringResource(id = R.string.male))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            )  {
                RadioButton(
                    selected = selectedGender == Gender.FEMALE,
                    onClick = { onGenderSelected(Gender.FEMALE) },
                    enabled = selectedGender != Gender.FEMALE
                )
                Text(stringResource(id = R.string.female))
            }
        }

    }
}