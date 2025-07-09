package com.example.v5rules.ui.compose.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.v5rules.data.DamageType
import com.example.v5rules.ui.theme.V5RulesTheme

@Composable
fun DamageTrackDisplay(
    damageTrack: List<DamageType>,
    onBoxClick: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
    allowsAggravated: Boolean = true,
    boxSize: Dp = 24.dp,
    boxPadding: Dp = 2.dp,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    superficialColor: Color = MaterialTheme.colorScheme.tertiary,
    aggravatedColor: Color = MaterialTheme.colorScheme.tertiary // O un altro colore scuro per distinguerlo
) {
    Row(modifier = modifier) {
        damageTrack.forEachIndexed { index, damageType ->
            DamageBox(
                damageType = damageType,
                onClick = { onBoxClick(index) },
                allowsAggravated = allowsAggravated,
                modifier = Modifier
                    .size(boxSize)
                    .padding(boxPadding),
                borderColor = borderColor,
                superficialColor = superficialColor,
                aggravatedColor = aggravatedColor
            )
        }
    }
}

@Composable
private fun DamageBox(
    damageType: DamageType,
    onClick: () -> Unit,
    allowsAggravated: Boolean,
    modifier: Modifier = Modifier,
    borderColor: Color = Color.Gray,
    superficialColor: Color = Color.Red,
    aggravatedColor: Color = Color.Red
) {
    Box(
        modifier = modifier
            .border(1.dp, borderColor)
            .clickable(onClick = onClick)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            when (damageType) {
                DamageType.EMPTY -> {
                    // Il bordo è già disegnato dal Modifier.border
                }
                DamageType.SUPERFICIAL -> {
                    // Disegna una singola linea diagonale (/)
                    drawLine(
                        color = superficialColor,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, 0f),
                        strokeWidth = Stroke.DefaultMiter
                    )
                }
                DamageType.AGGRAVATED -> {
                    if (allowsAggravated) {
                        // Disegna una X
                        // Linea \
                        drawLine(
                            color = aggravatedColor,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, size.height),
                            strokeWidth = Stroke.DefaultMiter
                        )
                        // Linea /
                        drawLine(
                            color = aggravatedColor,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, 0f),
                            strokeWidth = Stroke.DefaultMiter
                        )
                    } else {
                        drawLine(
                            color = superficialColor,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, 0f),
                            strokeWidth = Stroke.DefaultMiter
                        )
                    }
                }
            }
        }
    }
}

// --- Previews ---

@Preview(showBackground = true, name = "Health Track Preview (Allows Agg)")
@Composable
fun HealthTrackPreview() {
    V5RulesTheme {
        DamageTrackDisplay(
            damageTrack = listOf(
                DamageType.EMPTY,
                DamageType.SUPERFICIAL,
                DamageType.AGGRAVATED,
                DamageType.SUPERFICIAL,
                DamageType.EMPTY
            ),
            onBoxClick = {},
            allowsAggravated = true
        )
    }
}

@Preview(showBackground = true, name = "Willpower Track Preview (No Agg)")
@Composable
fun WillpowerTrackPreview() {
    V5RulesTheme {
        DamageTrackDisplay(
            damageTrack = listOf(
                DamageType.EMPTY,
                DamageType.SUPERFICIAL,
                DamageType.SUPERFICIAL, // Anche se fosse AGGRAVATED, si mostrerebbe come superficiale
                DamageType.EMPTY
            ),
            onBoxClick = {},
            allowsAggravated = false
        )
    }
}

@Preview(showBackground = true, name = "Empty Track")
@Composable
fun EmptyTrackPreview() {
    V5RulesTheme {
        DamageTrackDisplay(
            damageTrack = List(7) { DamageType.EMPTY },
            onBoxClick = {},
            allowsAggravated = true
        )
    }
}
