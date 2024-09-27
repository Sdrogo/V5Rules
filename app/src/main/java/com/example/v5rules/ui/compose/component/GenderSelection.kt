package com.example.v5rules.ui.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.v5rules.data.Gender
import com.example.v5rules.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenderSelection(
    selectedGender: Gender,
    widthOfFlow: Float,
    isLandscape: Boolean,
    onGenderSelected: (Gender) -> Unit
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth(widthOfFlow),
        maxItemsInEachRow = if(isLandscape) 3 else 1,
        horizontalArrangement = Arrangement.SpaceEvenly

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(if (isLandscape) 0.4f else 1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            RadioButton(
                selected = selectedGender == Gender.MALE,
                onClick = { onGenderSelected(Gender.MALE) },
                enabled = selectedGender != Gender.MALE
            )
            Text(modifier = Modifier.wrapContentSize(), text = stringResource(id = R.string.male))
        }
        Row(
            modifier = Modifier.fillMaxWidth(if (isLandscape) 0.4f else 1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            RadioButton(
                selected = selectedGender == Gender.FEMALE,
                onClick = { onGenderSelected(Gender.FEMALE) },
                enabled = selectedGender != Gender.FEMALE
            )
            Text(modifier = Modifier.wrapContentSize(), text = stringResource(id = R.string.female))
        }
    }
}