package com.example.v5rules.ui.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.example.v5rules.reader.ClanReader
import com.example.v5rules.reader.DisciplineReader

@Composable
fun DisciplineIcon(disciplineId: String, contentDescription: String?, size: Dp) {
    val imageResource =
        DisciplineReader.DisciplineImage.entries.find { it.disciplineId == disciplineId }?.imageResource
    imageResource?.let {
        Image(
            painter = painterResource(id = it),
            contentDescription = contentDescription,
            modifier = Modifier.size(size)
        )
    }
}

@Composable
fun ClanImage(clanName: String, tintColor: Color, width: Dp, isText: Boolean = false) {
    val nameImageResource =
        ClanReader.ClanImage.entries.find { it.clanName == clanName }?.nameImageResource
    val logoImageResource =
        ClanReader.ClanImage.entries.find { it.clanName == clanName }?.logoImageResource
    if (isText) {
        nameImageResource?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = "contentDescription",
                colorFilter = ColorFilter.tint(tintColor),
                modifier = Modifier.width(width)
            )
        }
    } else {
        logoImageResource?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = "contentDescription",
                colorFilter = ColorFilter.tint(tintColor),
                modifier = Modifier.width(width)
            )
        }
    }
}

@Composable
fun TintedImage(drawableId: Int, tintColor: Color, width: Dp) {
    Image(
        painter = painterResource(id = drawableId),
        contentDescription = "contentDescription",
        colorFilter = ColorFilter.tint(tintColor),
        modifier = Modifier.width(width)
    )
}