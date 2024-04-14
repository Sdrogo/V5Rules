package com.example.v5rules

import Content
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.v5rules.data.BottomItemDataClass
import com.example.v5rules.ui.compose.screen.ChapterScreen
import com.example.v5rules.ui.theme.V5RulesTheme
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException

class MainActivity : ComponentActivity() {

    private val bottomNavigationItems = listOf(
        BottomItemDataClass(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = "home"
        ),
        BottomItemDataClass(
            title = "Index",
            selectedIcon = Icons.Filled.List,
            unselectedIcon = Icons.Outlined.List,
            route = "index"
        ),
        BottomItemDataClass(
            title = "Search",
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search,
            route = "search"
        ),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainApp()
        }
    }

    @Composable
    fun MainApp() {
        val navController = rememberNavController()
        val selectedItemIndex = remember { mutableStateOf(0) }
        val content = remember { getContentFromJson() }

        V5RulesTheme {
            Scaffold(
                topBar = { 
                    Icon(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "")
                    Text(text = getString(R.string.app_name))
                         },
                bottomBar = {
                    NavigationBar {
                        bottomNavigationItems.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = selectedItemIndex.value == index,
                                onClick = {
                                    selectedItemIndex.value = index
                                    navController.navigate(item.route)
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (index == selectedItemIndex.value)
                                            item.selectedIcon
                                        else
                                            item.unselectedIcon,
                                        contentDescription = item.title
                                    )
                                }
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") { /* TODO: Implement HomeScreen */ }
                        composable("index") { /* TODO: Implement IndexScreen */ }
                        composable("search") { /* TODO: Implement SearchScreen */ }
                        content?.chapters?.forEach { chapter ->
                            composable("chapter/${chapter.title}") {
                                ChapterScreen(chapter = chapter, navController = navController)
                            }
                            chapter.sections.forEach { section ->
                                composable("section/${chapter.title}/${section.title}") {
                                    //SectionScreen(section = section, navController = navController)
                                }
                                section.paragraphs.forEach { paragraph ->
                                    composable("paragraph/${section.title}/${paragraph.title}") {
                                        //ParagraphScreen(paragraph = paragraph, navController = navController)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private fun getContentFromJson(): Content? {
        val jsonString = try {
            applicationContext.assets.open("content.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return Json.decodeFromString(jsonString)
    }
}
