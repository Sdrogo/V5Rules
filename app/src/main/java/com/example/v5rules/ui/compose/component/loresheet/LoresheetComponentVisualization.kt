package com.example.v5rules.ui.compose.component.loresheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.v5rules.R
import com.example.v5rules.data.Loresheet
import com.example.v5rules.ui.compose.component.ContentExpander
import com.example.v5rules.ui.compose.component.DotsWithMinMax

@Composable
fun LoresheetListVisualization(
    loresheets: List<Loresheet>
) {
   Column {
       loresheets.forEach { loresheet ->
           LoresheetItemVisualization(
               loresheet = loresheet
           )
       }
    }
}

@Composable
fun LoresheetItemVisualization(
    loresheet: Loresheet
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = loresheet.title,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f)
                )
                if (loresheet.level > 0) {
                    DotsWithMinMax(level = (loresheet.level))
                    Spacer(Modifier.width(8.dp))
                }
                Icon(imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse ${loresheet.title}" else "Expand ${loresheet.title}"
                )
            }
        }
        if (expanded) {

            ContentExpander(stringResource(R.string.discipline_description)) {
                Text(text = loresheet.content, style = MaterialTheme.typography.bodyMedium)
            }
        }
        LoresheetPowerSection(
            powers = loresheet.powers.subList(0, loresheet.level)
        )
    }
}
