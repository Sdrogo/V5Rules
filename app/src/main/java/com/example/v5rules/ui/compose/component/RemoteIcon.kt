package com.example.v5rules.ui.compose.component

import android.icu.text.ListFormatter.Width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun RemoteIcon(imageUrl: String, contentDescription: String?, size: Dp) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        modifier = Modifier.size(size)
    )
}

@Composable
fun TintedImage(imageUrl: String, tintColor: Color, width: Dp) {
            AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
            contentDescription = "Image Description",
            colorFilter = ColorFilter.tint(tintColor),
            modifier = Modifier.width(width)
            )
}