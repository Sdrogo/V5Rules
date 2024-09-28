package com.example.v5rules.ui.compose.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ContentExpander(
    title: String,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    fontWeight: FontWeight = FontWeight.Normal,
    initialState: Boolean = false,
    content: @Composable () -> Unit
) {
    var expandedDescription by remember { mutableStateOf(initialState) }
    Column(modifier = Modifier
        .padding(8.dp)
        .clickable { expandedDescription = !expandedDescription }) {
        Text(
            text = title,
            style = style,
            fontWeight = fontWeight,
            modifier = Modifier.clickable { expandedDescription = !expandedDescription },
            color = if (expandedDescription) {
                MaterialTheme.colorScheme.tertiary
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
        AnimatedVisibility(visible = expandedDescription,
            enter = fadeIn(animationSpec = tween(easing = FastOutSlowInEasing, durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(easing = FastOutSlowInEasing, durationMillis = 300))) {
            Spacer(modifier = Modifier.height(16.dp).width(8.dp))
            content()
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}
@Composable
fun CustomContentExpander(
    maxWith: Float,
    initialState: Boolean = false,
    header: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    var expandedDescription by remember { mutableStateOf(initialState) }
    Column(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth(maxWith)
        .clickable { expandedDescription = !expandedDescription })
    {
        header()
        AnimatedVisibility(visible = expandedDescription,
            enter = fadeIn(animationSpec = tween(easing = FastOutSlowInEasing, durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(easing = FastOutSlowInEasing, durationMillis = 300))) {
            content()
        }
    }
}