package com.example.v5rules.ui.compose.screen.pg

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.R
import com.example.v5rules.SubPgNav
import com.example.v5rules.data.Paragraph
import com.example.v5rules.ui.compose.component.ContentExpander
import com.example.v5rules.ui.compose.component.TableContent
import com.example.v5rules.viewModel.PgViewModel

@Composable
fun PgDetailsScreen(
    pgViewModel: PgViewModel,
    navController: NavHostController,
    title: String,
    onTitleChanged: (String) -> Unit
) {

    val rule = pgViewModel.allPg.find { it.title == title }
    LaunchedEffect(Unit) {
        onTitleChanged(title)
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        rule?.let { rule ->
            rule.sections.let { sections ->
                item {
                    if (rule.content != "") {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = AnnotatedString(
                                rule.content,
                                paragraphStyle = ParagraphStyle(textAlign = TextAlign.Justify)
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                items(sections.orEmpty()) { section ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        if (section.subParagraphs != null) {
                            Text(
                                text = section.title,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable {
                                        navController.navigate(
                                            SubPgNav(rule.title, section.title)
                                        )
                                    },
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            PgParagraphContent(section)
                        }
                    }
                }
                rule.table?.let {
                    item {
                        TableContent(headerList = it.headers, contentList = it.columns)
                    }
                }
            }

        }
    }
}

@Composable
fun SubPgDetail(
    pgViewModel: PgViewModel,
    chapterTitle: String,
    sectionTitle: String,
    onTitleChanged: (String) -> Unit
) {

    val ruleToDetail = pgViewModel.allPg.find { it.title == chapterTitle }
    val sectionToDetail = ruleToDetail?.sections?.find { it.title == sectionTitle }
    LaunchedEffect(Unit) {
        onTitleChanged(sectionTitle)
    }
    sectionToDetail?.let { section ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            item {
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
                    section.table?.let {
                        TableContent(
                            headerList = it.headers,
                            contentList = it.columns
                        )
                    }
                }
            }
            items(section.subParagraphs.orEmpty()) { subParagraph ->
                PgParagraphContent(subParagraph)

                // Nested Row for subParagraphs (if needed)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    Row {
                        Spacer(
                            modifier = Modifier
                                .width(2.dp)
                                .fillMaxHeight()
                                .background(colorResource(id = R.color.accentColor))
                        )
                        Column {
                            subParagraph.subParagraphs?.forEach { subSubParagraph ->
                                PgParagraphContent(subSubParagraph)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PgParagraphContent(paragraph: Paragraph) {
    ContentExpander(
        title = paragraph.title,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = AnnotatedString(
                    paragraph.content,
                    paragraphStyle = ParagraphStyle(textAlign = TextAlign.Justify)
                ),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
            paragraph.table?.let {
                TableContent(
                    headerList = it.headers,
                    contentList = it.columns
                )
            }
        }
    }
}