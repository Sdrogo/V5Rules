package com.example.v5rules.ui.compose.screen.background

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.ui.compose.component.RangeDots
import com.example.v5rules.viewModel.BackgroundUiState
import com.example.v5rules.viewModel.BackgroundViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BackgroundDetailsScreen(
    id: Int,
    name: String,
    backgroundViewModel: BackgroundViewModel,
    navController: NavHostController,
) {
    CommonScaffold(
        navController = navController,
        title = name
    ) {
        val uiState by backgroundViewModel.backgroundUiState.collectAsState()
        val background = (uiState as BackgroundUiState.Success).backgrounds.first { it.id == id }

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
                    background.prerequisites?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary

                        )
                    }
                    Surface(
                        modifier = Modifier
                            .padding(8.dp)
                            .wrapContentSize()
                            .background(MaterialTheme.colorScheme.background)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.tertiary,
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
            items(background.merits.orEmpty()) { merit ->
                Spacer(modifier = Modifier.height(8.dp))
                Column {

                    FlowRow(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = merit.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.wrapContentWidth()
                        )
                        Spacer(Modifier.width(16.dp))
                        merit.minLevel?.let { min ->
                            merit.maxLevel?.let { max ->
                                RangeDots(min, max)
                                Spacer(Modifier.width(16.dp))
                            }
                        }
                        merit.prerequisites?.let { prerequisite ->
                            Text(
                                text = prerequisite,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary,
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
                                MaterialTheme.colorScheme.tertiary,
                                RoundedCornerShape(8.dp)
                            ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = AnnotatedString(merit.description,
                                paragraphStyle = ParagraphStyle(textAlign = TextAlign.Justify)
                            ),
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                }
            }
            items(background.directFlaws.orEmpty()) { flaw ->
                Column {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = flaw.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.wrapContentWidth()
                        )
                        Spacer(Modifier.width(16.dp))
                        flaw.minLevel?.let { min ->
                            flaw.maxLevel?.let { max ->
                                RangeDots(min, max)
                                Spacer(Modifier.width(16.dp))
                            }
                        }
                        flaw.prerequisites?.let { prerequisite ->
                            Text(
                                text = prerequisite,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary,
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
                                MaterialTheme.colorScheme.tertiary,
                                RoundedCornerShape(8.dp)
                            ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = AnnotatedString(flaw.description,
                                paragraphStyle = ParagraphStyle(textAlign = TextAlign.Justify)
                            ),
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                }
            }
            items(background.flaws.orEmpty()) { flaw ->
                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = flaw.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.wrapContentWidth()
                        )
                        Spacer(Modifier.width(16.dp))
                        flaw.minLevel?.let { min ->
                            flaw.maxLevel?.let { max ->
                                RangeDots(min, max)
                            }
                        }
                    }
                    Surface(
                        modifier = Modifier
                            .padding(8.dp)
                            .wrapContentSize()
                            .background(MaterialTheme.colorScheme.background)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.tertiary,
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