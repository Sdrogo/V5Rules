package com.example.v5rules.ui.compose.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DotsForLevel(level: Int, isLandscape: Boolean = false, onClick: () -> Unit) {

    val widthValue = if (isLandscape) 0.3f else 1f
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth(widthValue)
            .clickable { onClick() }
    ) {
        Text(
            text = "Level $level",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .wrapContentWidth()
                .padding(8.dp)
        )
        Spacer(
            modifier = Modifier
                .weight(0.5f)
        )
        for (i in 1..level) Text(
            "●",
            color = MaterialTheme.colorScheme.tertiary,
        )
        for (i in level..5) Text(
            "○",
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}

@Composable
fun RangeDots(min: Int, max: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .wrapContentWidth()
            .padding(horizontal = 8.dp)
    ) {
        if(min != max){
            Text(
                text = "(",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(horizontal = 8.dp)
            )
        }
        for (i in 1..min) Text(
            "●",
            color = MaterialTheme.colorScheme.tertiary,
        )
        if(min != max){
            Text(
                text = " - ",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(horizontal = 8.dp)
            )
            for (i in 1..max) Text(
                "●",
                color = MaterialTheme.colorScheme.tertiary,
            )
            Text(
                text = ")",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun DotsForAttribute(
    label: String,
    level: Int,
    textStyle: TextStyle = MaterialTheme.typography.headlineSmall
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .wrapContentWidth()
    ) {
        Text(
            text = label,
            style = textStyle,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
        Spacer(
            modifier = Modifier
                .weight(0.5f)
        )
        for (i in 1..level) Text(
            "●",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 24.sp // Imposta la dimensione del font desiderata
        )
        for (i in level + 1..5) Text(
            "○",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 24.sp // Imposta la dimensione del font desiderata
        )
    }
}

@Composable
fun DotsOnlyForLevel(level: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .wrapContentWidth()
    ) {
        for (i in 1..level) Text(
            "●",
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}


@Composable
fun DotsWithMinMax(
    level: Int = 1,
    maxLevel: Int = 5,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .wrapContentWidth()
    ) {
        for (i in 1..(level?:1)) Text(
            "●",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 24.sp // Imposta la dimensione del font desiderata
        )
        for (i in (level?:1) + 1..(maxLevel ?:5)) Text(
            "○",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 24.sp // Imposta la dimensione del font desiderata
        )
    }
}
