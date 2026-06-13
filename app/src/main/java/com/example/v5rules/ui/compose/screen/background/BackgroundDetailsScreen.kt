package com.example.v5rules.ui.compose.screen.background

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.v5rules.data.Advantage
import com.example.v5rules.ui.compose.component.DotsWithMinMax
import com.example.v5rules.ui.compose.component.RangeDots
import com.example.v5rules.viewModel.BackgroundUiState
import com.example.v5rules.viewModel.BackgroundViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BackgroundDetailsScreen(
    id: String,
    name: String,
    backgroundViewModel: BackgroundViewModel,
    onTitleChanged: (String) -> Unit
) {
    LaunchedEffect(Unit) {
        onTitleChanged(name)
    }
    val uiState by backgroundViewModel.backgroundUiState.collectAsState()

    when (val state = uiState) {
        is BackgroundUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is BackgroundUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = state.message, color = MaterialTheme.colorScheme.error)
            }
        }

        is BackgroundUiState.Success -> {
            val background = state.backgrounds.firstOrNull { it.id == id }
            if (background == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Background non trovato")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .background(color = MaterialTheme.colorScheme.background)
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 8.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                Text(
                                    text = background.title,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.wrapContentWidth()
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                RangeDots(
                                    min = background.minLevel,
                                    max = background.maxLevel
                                )
                            }

                            background.prerequisites?.let {
                                Text(
                                    text = "Prerequisito: $it",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.wrapContentWidth()
                                )
                            }
                            Surface(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .wrapContentSize()
                                    .background(MaterialTheme.colorScheme.background)
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.secondary,
                                        RoundedCornerShape(8.dp)
                                    ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = background.description,
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                    items(background.merits) { merit ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Column {
                            FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                // titolo e pallini come figli di FlowRow così possono andare a capo separatamente
                                Text(
                                    text = merit.title,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.wrapContentWidth()
                                )

                                merit.minLevel?.let { min ->
                                    merit.maxLevel?.let { max ->
                                        RangeDots(min, max, modifier = Modifier)
                                    }
                                }
                                merit.prerequisites?.let { prerequisite ->
                                    Text(
                                        text = "Prerequisito: $prerequisite",
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.wrapContentWidth()
                                    )
                                }

                            }

                            Surface(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .wrapContentSize()
                                    .background(MaterialTheme.colorScheme.background)
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.secondary,
                                        RoundedCornerShape(8.dp)
                                    ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = AnnotatedString(
                                        merit.description,
                                        paragraphStyle = ParagraphStyle(textAlign = TextAlign.Justify)
                                    ),
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                    items(background.directFlaws) { flaw ->
                        Column {
                            FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = flaw.title,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.wrapContentWidth()
                                )

                                flaw.minLevel?.let { min ->
                                    flaw.maxLevel?.let { max ->
                                        RangeDots(min, max, modifier = Modifier)
                                    }

                                }

                                flaw.prerequisites?.let { prerequisite ->
                                    Text(
                                        text = prerequisite,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 4.dp, start = 0.dp)
                                    )
                                }
                            }

                            // Prerequisite on its own line

                            Surface(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .wrapContentSize()
                                    .background(MaterialTheme.colorScheme.background)
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.secondary,
                                        RoundedCornerShape(8.dp)
                                    ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = AnnotatedString(
                                        flaw.description,
                                        paragraphStyle = ParagraphStyle(textAlign = TextAlign.Justify)
                                    ),
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                    items(background.flaws) { flaw ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Column {
                            FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = flaw.title,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.wrapContentWidth()
                                )

                                flaw.minLevel?.let { min ->
                                    flaw.maxLevel?.let { max ->
                                        RangeDots(min, max, modifier = Modifier)
                                    }
                                }
                            }

                            // Prerequisite on its own line
                            flaw.prerequisites?.let { prerequisite ->
                                Text(
                                    text = prerequisite,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp, start = 0.dp)
                                )
                            }

                            Surface(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .wrapContentSize()
                                    .background(MaterialTheme.colorScheme.background)
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.secondary,
                                        RoundedCornerShape(8.dp)
                                    ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = flaw.description,
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DirectFlawDetailsScreen(
    flaw: Advantage,
    onTitleChanged: (String) -> Unit
) {
    LaunchedEffect(Unit) {
        onTitleChanged(flaw.title)
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp)
            ) {
                flaw.prerequisites?.let {

                    Text(
                        text = "Prerequisito: $it",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.wrapContentWidth()
                    )
                }
                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .wrapContentSize()
                        .background(MaterialTheme.colorScheme.background)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.secondary,
                            RoundedCornerShape(8.dp)
                        ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = flaw.description,
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}
