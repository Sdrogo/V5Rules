package com.example.v5rules.ui.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TableContent(headerList: List<String>, contentList: List<List<String>>) {
    val accentColor = MaterialTheme.colorScheme.secondary
    val textColor = MaterialTheme.colorScheme.primary
    val backgroundColor = MaterialTheme.colorScheme.background
    Column {
        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, accentColor)
                .background(backgroundColor),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            headerList.forEachIndexed { index, header ->
                Text(
                    text = header,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .drawBehind { // Draw the border to the right
                            if (index < headerList.size - 1) {
                                val strokeWidth = 1.dp.toPx()
                                val yStart = 0f
                                val yEnd = size.height
                                drawLine(
                                    color = accentColor,
                                    start = Offset(size.width - strokeWidth / 2, yStart),
                                    end = Offset(size.width - strokeWidth / 2, yEnd),
                                    strokeWidth = strokeWidth
                                )
                            }
                        }
                )
            }
        }
        contentList.forEachIndexed { _, row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .border(
                        width = 1.dp,
                        color = accentColor
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val maxHeight = row.maxOfOrNull { it.length }?.dp ?: 0.dp
                row.forEachIndexed { index, cell ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                            .drawBehind {
                                // Draw the vertical borders
                                if (index < row.size - 1) {
                                    val strokeWidth = 1.dp.toPx()
                                    val yStart = 0f
                                    val yEnd = maxHeight.toPx() // Use the actual height of the cell
                                    drawLine(
                                        color = accentColor,
                                        start = Offset(size.width - strokeWidth / 2, yStart),
                                        end = Offset(size.width - strokeWidth / 2, yEnd),
                                        strokeWidth = strokeWidth
                                    )
                                }
                            }
                    ) {
                        Text(
                            text = cell,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth(),
                            color = textColor
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
