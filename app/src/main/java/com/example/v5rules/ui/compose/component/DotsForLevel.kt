package com.example.v5rules.ui.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

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
        Row {
            for (i in 1..5) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(
                            if (i <= level) MaterialTheme.colorScheme.secondary else Color.Transparent
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                )
            }
        }
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
        if (min != max) {
            Text(
                text = "(",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(horizontal = 8.dp)
            )
        }
        Row {
            (1..min).forEach { i ->
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.secondary
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                )
            }
        }
        if (min != max) {
            Text(
                text = " - ",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(horizontal = 8.dp)
            )
            Row {
                (1..max).forEach { i ->
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(
                                MaterialTheme.colorScheme.secondary
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                    )
                }
            }
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
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            style = textStyle,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row {
            for (i in 1..5) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(
                            if (i <= level) MaterialTheme.colorScheme.secondary else Color.Transparent
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Composable
fun DotsOnlyForLevel(level: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .wrapContentWidth()
    ) {
        Row {
            (1..level).forEach { i ->
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.secondary
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                )
            }
        }
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
        Row {
            for (i in 1..maxLevel) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(
                            if (i <= level) MaterialTheme.colorScheme.secondary else Color.Transparent
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}
