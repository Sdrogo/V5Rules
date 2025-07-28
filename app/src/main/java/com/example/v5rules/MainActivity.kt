package com.example.v5rules

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.v5rules.ui.theme.V5RulesTheme
import com.example.v5rules.viewModel.BackgroundViewModel
import com.example.v5rules.viewModel.ClanViewModel
import com.example.v5rules.viewModel.DisciplineViewModel
import com.example.v5rules.viewModel.KindredViewModel
import com.example.v5rules.viewModel.LoreViewModel
import com.example.v5rules.viewModel.LoresheetViewModel
import com.example.v5rules.viewModel.NPCGeneratorViewModel
import com.example.v5rules.viewModel.PgViewModel
import com.example.v5rules.viewModel.PredatorTypeViewModel
import com.example.v5rules.viewModel.RulesViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint

val LocalAuthUser = compositionLocalOf<FirebaseUser?> { null }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called")
        setContent {
            V5RulesTheme {
                V5RulesApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun V5RulesApp() {
    // ViewModels
    val disciplineViewModel: DisciplineViewModel = hiltViewModel()
    val clanViewModel: ClanViewModel = hiltViewModel()
    val predatorTypeViewModel: PredatorTypeViewModel = hiltViewModel()
    val rulesViewModel: RulesViewModel = hiltViewModel()
    val loreViewModel: LoreViewModel = hiltViewModel()
    val npcGeneratorViewModel: NPCGeneratorViewModel = hiltViewModel()
    val loresheetViewModel: LoresheetViewModel = hiltViewModel()
    val kindredViewModel: KindredViewModel = hiltViewModel()
    val pgViewModel: PgViewModel = hiltViewModel()
    val backgroundViewModel: BackgroundViewModel = hiltViewModel()

    val navController = rememberNavController()
    var currentUser by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }

    // State for the Scaffold
    var currentTitle by remember { mutableStateOf("") }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val canNavigateBack = navBackStackEntry?.destination?.route != HomeNav.javaClass.name &&
            navBackStackEntry?.destination?.route != LoginNav.javaClass.name

    // Listener per lo stato di autenticazione
    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            currentUser = auth.currentUser
        }
        FirebaseAuth.getInstance().addAuthStateListener(listener)
        onDispose {
            FirebaseAuth.getInstance().removeAuthStateListener(listener)
        }
    }

    val isLoggedIn = currentUser?.uid != null

    CompositionLocalProvider(LocalAuthUser provides currentUser) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = currentTitle,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        if (canNavigateBack) {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Go Back",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    },
                    actions = {
                        if(isLoggedIn){
                            IconButton(onClick = { navController.navigate(UserProfileNav) }) {
                                Icon(
                                    imageVector = Icons.Filled.AccountCircle,
                                    contentDescription = "User Profile",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        titleContentColor = MaterialTheme.colorScheme.secondary
                    ),
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
            ) {
                CustomNavHost(
                    navController = navController,
                    onTitleChanged = { title -> currentTitle = title }, // Passa la callback
                    disciplineViewModel = disciplineViewModel,
                    clanViewModel = clanViewModel,
                    predatorTypeViewModel = predatorTypeViewModel,
                    rulesViewModel = rulesViewModel,
                    loreViewModel = loreViewModel,
                    loresheetViewModel = loresheetViewModel,
                    npcGeneratorViewModel = npcGeneratorViewModel,
                    kindredViewModel = kindredViewModel,
                    pgViewModel = pgViewModel,
                    backgroundViewModel = backgroundViewModel
                )
            }
        }
    }
}