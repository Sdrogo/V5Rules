package com.example.v5rules.ui.compose.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.v5rules.R
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.ui.compose.component.ContentExpander
import com.example.v5rules.ui.compose.component.TextBlock
import com.example.v5rules.ui.viewModel.PredatorTypeViewModel

@Composable
fun PredatorTypeDetailsScreen(
    predatorTypeViewModel: PredatorTypeViewModel,
    navController: NavHostController,
    typeName: String
) {

    val predator = predatorTypeViewModel.allTypes.find { it.name == typeName }

    CommonScaffold(navController = navController, title = typeName) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            predator?.let {
                item {
                    ContentExpander(
                        title = stringResource(id = R.string.predator_type_screen_description),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold) {
                        Text(
                            text = AnnotatedString(
                                it.description,
                                paragraphStyle = ParagraphStyle(textAlign = TextAlign.Justify)
                            ),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    TextBlock(title = stringResource(id = R.string.predator_type_screen_hunt_pool), component = it.huntPool, isHidden = false)
                    ContentExpander(
                        title = stringResource(id = R.string.predator_type_screen_detail),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        initialState = true
                    ) {
                        Surface(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .border(
                                    1.dp,
                                    colorResource(id = R.color.background_red),
                                    RoundedCornerShape(8.dp)
                                ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column {
                                it.paragraphs.forEach {
                                   Row (modifier = Modifier.padding(8.dp)){
                                       Text(text = "●")
                                       Text(
                                           text = AnnotatedString(
                                               it,
                                               paragraphStyle = ParagraphStyle(textAlign = TextAlign.Justify)
                                           ),
                                           style = MaterialTheme.typography.bodyMedium,
                                           fontSize = 16.sp,
                                           modifier = Modifier.padding(horizontal = 16.dp)
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