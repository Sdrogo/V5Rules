package com.example.v5rules.ui.compose.screen.clan

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.v5rules.R
import com.example.v5rules.data.Clan
import com.example.v5rules.ui.compose.component.ClanImage
import com.example.v5rules.ui.compose.component.ContentExpander
import com.example.v5rules.ui.compose.component.TextBlock
import com.example.v5rules.viewModel.ClanViewModel

@Composable
fun ClanDetailScreen(
    clanViewModel: ClanViewModel,
    clanName: String,
    onTitleChanged: (String) -> Unit
) {
    val clan = clanViewModel.allClans.find { it.name == clanName }
    LaunchedEffect(Unit) {
        onTitleChanged(clanName)
    }
    clan?.let { clan ->
        ClanDetail(clan = clan)
    }
}

@Composable
fun ClanDetail(
    clan: Clan
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        item {
            Spacer(modifier = Modifier.padding(8.dp))
            Row {
                Spacer(modifier = Modifier.weight(1f))
                ClanImage(
                    clanName = clan.name,
                    tintColor = MaterialTheme.colorScheme.tertiary,
                    width = 200.dp
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Row {
                Spacer(modifier = Modifier.weight(1f))
                ClanImage(
                    clanName = clan.name,
                    tintColor = MaterialTheme.colorScheme.tertiary,
                    width = 400.dp,
                    true
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.padding(16.dp))
            ContentExpander(
                title = stringResource(id = R.string.clan_description, clan.name),
                style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold
            ) {
                Text(
                    text = AnnotatedString(
                        clan.description,
                        paragraphStyle = ParagraphStyle(textAlign = TextAlign.Justify)
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
        items(clan.paragraphs) {
            ContentExpander(
                title = it.title,
                style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold
            ) {
                Column {
                    Text(
                        text = AnnotatedString(
                            it.content,
                            paragraphStyle = ParagraphStyle(textAlign = TextAlign.Justify)
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    it.subParagraphs.let { sub ->
                        sub?.forEach { subItem ->
                            ContentExpander(
                                title = subItem.title,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            ) {
                                Text(
                                    text = AnnotatedString(
                                        subItem.content,
                                        paragraphStyle = ParagraphStyle(textAlign = TextAlign.Justify)
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
        item {
            ContentExpander(
                title = stringResource(id = R.string.clan_disciplines),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            ) {
                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.tertiary,
                            RoundedCornerShape(8.dp)
                        ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
                        clan.disciplines.forEach { discipline ->
                            TextBlock(
                                title = "${discipline.title}:",
                                component = discipline.content,
                                isHidden = discipline.content.isEmpty()
                            )
                        }
                    }
                }
            }
        }
        item {
            clan.weakness.let {
                ContentExpander(
                    title = stringResource(id = R.string.clan_weakness),
                    style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold
                ) {
                    Text(
                        text = AnnotatedString(
                            it,
                            paragraphStyle = ParagraphStyle(textAlign = TextAlign.Justify)
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
        items(clan.compulsion) {
            ContentExpander(
                title = stringResource(id = R.string.clan_compulsion, it.name),
                style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold
            ) {
                Text(
                    text = AnnotatedString(
                        it.description,
                        paragraphStyle = ParagraphStyle(textAlign = TextAlign.Justify)
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

}