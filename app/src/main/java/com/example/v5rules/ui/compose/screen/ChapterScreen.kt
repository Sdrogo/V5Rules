package com.example.v5rules.ui.compose.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.v5rules.data.Chapter
import com.example.v5rules.data.Paragraph
import com.example.v5rules.data.Section


@Composable
fun ChapterScreen(chapter: Chapter, navController: NavController) {
    LazyColumn {
        item {
            Text(text = chapter.title)
            Text(text = chapter.content ?: "")
        }
        items(chapter.sections.size) { index ->
            val section = chapter.sections[index]
            SectionLink(section = section, navController = navController)
        }
    }
}

@Composable
fun SectionLink(section: Section, navController: NavController) {
    Column {
        Text(
            text = section.title,
            modifier = Modifier.clickable {
                navController.navigate("section/${section.title}")
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column {
            section.paragraphs.forEach { paragraph ->
                ParagraphLink(paragraph = paragraph, navController = navController)
            }
        }
    }
}
@Composable
fun ParagraphLink(paragraph: Paragraph, navController: NavController) {
    Text(
        text = paragraph.title,
        modifier = Modifier.clickable {
            navController.navigate("paragraph/${paragraph.title}")
        }
    )
    Text(text = paragraph.content ?: "")
}

@Preview
@Composable
fun ChapterScreenPreview() {
    val navController = rememberNavController()
    val chapter = Chapter(
        title = "Chapter 1",
        content = "This is the content of Chapter 1",
        sections = listOf(
            Section(
                title = "Section 1.1",
                paragraphs = listOf(
                    Paragraph(
                        title = "Paragraph 1.1.1",
                        content = "Content of Paragraph 1.1.1"
                    ),
                    Paragraph(
                        title = "Paragraph 1.1.2",
                        content = "Content of Paragraph 1.1.2"
                    )
                )
            ),
            Section(
                title = "Section 1.2",
                paragraphs = listOf(
                    Paragraph(
                        title = "Paragraph 1.2.1",
                        content = "Content of Paragraph 1.2.1"
                    ),
                    Paragraph(
                        title = "Paragraph 1.2.2",
                        content = "Content of Paragraph 1.2.2"
                    )
                )
            )
        )
    )

    Column {
        Text(text = chapter.title)
        Text(text = chapter.content ?: "")
        LazyColumn {
            items(chapter.sections.size) { index ->
                val section = chapter.sections[index]
                SectionLink(section = section, navController = navController)
            }
        }
    }
}