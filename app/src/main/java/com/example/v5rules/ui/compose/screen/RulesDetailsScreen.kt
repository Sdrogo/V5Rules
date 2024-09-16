package com.example.v5rules.ui.compose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.R
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.ui.compose.component.ContentExpander
import com.example.v5rules.ui.viewModel.RulesViewModel

@Composable
fun RulesDetailsScreen(
    rulesViewModel: RulesViewModel,
    navController: NavHostController,
    title: String
) {

    val rule = rulesViewModel.allRules.find { it.title == title }

    CommonScaffold(navController = navController, title = title) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .background(color = MaterialTheme.colorScheme.secondary)
        ) {
            rule?.let { rule ->
                rule.sections.let { sections ->
                    item {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text= AnnotatedString(
                                rule.content,
                                paragraphStyle = ParagraphStyle(textAlign = TextAlign.Justify)
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    items(sections.orEmpty()) { section ->
                        ContentExpander(
                            title = section.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = AnnotatedString(
                                        section.content,
                                        paragraphStyle = ParagraphStyle(textAlign = TextAlign.Justify)
                                    ),
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Column(modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Min)
                                ) {
                                    Row {
                                        Spacer(modifier = Modifier
                                            .width(2.dp)
                                            .fillMaxHeight()
                                            .background(colorResource(id = R.color.accentColor))
                                        )
                                        Column {
                                            section.subParagraphs?.forEach { subSection ->
                                                ContentExpander(
                                                    title = subSection.title,
                                                    style = MaterialTheme.typography.headlineSmall,
                                                    fontWeight = FontWeight.Bold
                                                ) {
                                                    Text(
                                                        modifier = Modifier.padding(start = 8.dp),
                                                        text = AnnotatedString(
                                                            subSection.content,
                                                            paragraphStyle = ParagraphStyle(textAlign = TextAlign.Justify)
                                                        ),
                                                        color = MaterialTheme.colorScheme.primary,
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )
                                                }
                                                // Nested Row for subParagraphs (if needed)
                                                Column(modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(IntrinsicSize.Min)
                                                ) {
                                                    Row {
                                                        Spacer(modifier = Modifier
                                                            .width(2.dp)
                                                            .fillMaxHeight()
                                                            .background(colorResource(id = R.color.accentColor))
                                                        )
                                                        Column {
                                                            subSection.subParagraphs?.forEach { subParagraph ->
                                                                ContentExpander(
                                                                    title = subParagraph.title,
                                                                    style = MaterialTheme.typography.headlineSmall,
                                                                    fontWeight = FontWeight.Bold
                                                                ) {
                                                                    Text(
                                                                        modifier = Modifier.padding(start = 8.dp),
                                                                        text = AnnotatedString(
                                                                            subParagraph.content,
                                                                            paragraphStyle = ParagraphStyle(textAlign = TextAlign.Justify)
                                                                        ),
                                                                        color = MaterialTheme.colorScheme.primary,
                                                                        style = MaterialTheme.typography.bodyMedium
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}