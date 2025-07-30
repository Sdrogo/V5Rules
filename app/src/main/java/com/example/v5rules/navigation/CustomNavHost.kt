package com.example.v5rules.navigation

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.v5rules.ui.compose.screen.HomeScreen
import com.example.v5rules.ui.compose.screen.LoginScreen
import com.example.v5rules.ui.compose.screen.NPCGeneratorScreen
import com.example.v5rules.ui.compose.screen.background.BackgroundDetailsScreen
import com.example.v5rules.ui.compose.screen.background.BackgroundScreen
import com.example.v5rules.ui.compose.screen.clan.ClanDetailScreen
import com.example.v5rules.ui.compose.screen.clan.ClanListScreen
import com.example.v5rules.ui.compose.screen.discipline.DisciplineDetailScreen
import com.example.v5rules.ui.compose.screen.discipline.DisciplinePowerScreen
import com.example.v5rules.ui.compose.screen.discipline.DisciplineScreen
import com.example.v5rules.ui.compose.screen.discipline.RitualScreen
import com.example.v5rules.ui.compose.screen.kindred.KindredDetailsScreen
import com.example.v5rules.ui.compose.screen.kindred.KindredListScreen
import com.example.v5rules.ui.compose.screen.kindred.SubKindredDetail
import com.example.v5rules.ui.compose.screen.lore.LoreDetailsScreen
import com.example.v5rules.ui.compose.screen.lore.LoreListScreen
import com.example.v5rules.ui.compose.screen.lore.SubLoreDetail
import com.example.v5rules.ui.compose.screen.loresheet.LoresheetDetailsScreen
import com.example.v5rules.ui.compose.screen.loresheet.LoresheetScreen
import com.example.v5rules.ui.compose.screen.pg.PgDetailsScreen
import com.example.v5rules.ui.compose.screen.pg.PgListScreen
import com.example.v5rules.ui.compose.screen.pg.SubPgDetail
import com.example.v5rules.ui.compose.screen.predator.PredatorTypeDetailsScreen
import com.example.v5rules.ui.compose.screen.predator.PredatorTypeListScreen
import com.example.v5rules.ui.compose.screen.rule.RuleListScreen
import com.example.v5rules.ui.compose.screen.rule.RulesDetailsScreen
import com.example.v5rules.ui.compose.screen.rule.SubRuleDetail
import com.example.v5rules.ui.compose.screen.sheet.CharacterSheetListScreen
import com.example.v5rules.ui.compose.screen.sheet.CharacterSheetScreen
import com.example.v5rules.ui.compose.screen.sheet.visualization.CharacterSheetScreenVisualization
import com.example.v5rules.ui.compose.screen.user.FriendRequestsScreen
import com.example.v5rules.ui.compose.screen.user.UserProfileScreen
import com.example.v5rules.viewModel.BackgroundViewModel
import com.example.v5rules.viewModel.CharacterSheetViewModel
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
import kotlinx.serialization.Serializable

@Serializable
object HomeNav

@Serializable
object LoginNav

@Serializable
object DisciplinesNav

@Serializable
object PredatorTypesNav

@Serializable
object ClansNav

@Serializable
object LoreNav

@Serializable
object KindredNav

@Serializable
object PgNav

@Serializable
object NPCGeneratorNav

@Serializable
object RulesNav

@Serializable
object LoresheetNav

@Serializable
object BackgroundNav

@Serializable
object CharacterSheetListNav

@Serializable
object CharacterSheetCreationNav

@Serializable
object UserProfileNav

@Serializable
object FriendRequestsNav

@Serializable
data class DisciplineDetailsNav(val disciplineId: String)

@Serializable
data class DisciplinePowerNav(val disciplineId: String, val subDisciplineId: String)

@Serializable
data class RitualNav(val disciplineId: String, val ritualId: String)

@Serializable
data class PredatorTypeDetailsNav(val predatorName: String)

@Serializable
data class ClanDetailsNav(val clanName: String)

@Serializable
data class LoreDetailsNav(val title: String)

@Serializable
data class SubLoreNav(val title: String, val section: String)

@Serializable
data class KindredDetailsNav(val title: String)

@Serializable
data class SubKindredNav(val title: String, val section: String)

@Serializable
data class PgDetailsNav(val title: String)

@Serializable
data class SubPgNav(val title: String, val section: String)

@Serializable
data class RulesDetailsNav(val title: String)

@Serializable
data class SubRuleNav(val title: String, val section: String)

@Serializable
data class LoresheetDetailsNav(val name: String, val id: String)

@Serializable
data class BackgroundDetailsNav(val name: String, val id: String)

@Serializable
data class CharacterSheetEditNav(val id: String)

@Serializable
data class CharacterSheetVisualizationNav(val id: String)


@Composable
fun CustomNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: Any,
    onTitleChanged: (String) -> Unit,
    disciplineViewModel: DisciplineViewModel,
    clanViewModel: ClanViewModel,
    predatorTypeViewModel: PredatorTypeViewModel,
    rulesViewModel: RulesViewModel,
    loreViewModel: LoreViewModel,
    loresheetViewModel: LoresheetViewModel,
    npcGeneratorViewModel: NPCGeneratorViewModel,
    kindredViewModel: KindredViewModel,
    pgViewModel: PgViewModel,
    backgroundViewModel: BackgroundViewModel
) {

    NavHost(navController = navController, startDestination = startDestination, modifier = modifier)
    {
        val enterTransition = fadeIn(
            animationSpec = tween(
                durationMillis = 500,
                delayMillis = 100,
                easing = LinearOutSlowInEasing
            ),
            initialAlpha = 0.0f
        )
        val exitTransition = fadeOut(
            animationSpec = tween(
                durationMillis = 300,
                delayMillis = 0,
                easing = FastOutLinearInEasing
            ),
            targetAlpha = 0f
        )

        composable<HomeNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }
        ) {
            HomeScreen(navController, onTitleChanged)
        }

        composable<LoginNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }
        ) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(HomeNav)
                },
                onTitleChanged = onTitleChanged
            )
        }
        composable<BackgroundNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }
        ) {
            BackgroundScreen(backgroundViewModel, navController, onTitleChanged = onTitleChanged)
        }
        composable<DisciplinesNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) {
            DisciplineScreen(
                disciplineViewModel, navController,
                onTitleChanged = onTitleChanged
            )
        }
        composable<DisciplineDetailsNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<DisciplineDetailsNav>()
            DisciplineDetailScreen(
                disciplineId = entry.disciplineId,
                disciplineViewModel,
                navController,
                onTitleChanged = onTitleChanged
            )
        }
        composable<DisciplinePowerNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<DisciplinePowerNav>()
            DisciplinePowerScreen(
                disciplineId = entry.disciplineId,
                disciplinePowerId = entry.subDisciplineId,
                viewModel = disciplineViewModel,
                onTitleChanged = onTitleChanged
            )
        }
        composable<RitualNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<RitualNav>()
            RitualScreen(
                disciplineId = entry.disciplineId,
                ritualId = entry.ritualId,
                viewModel = disciplineViewModel,
                onTitleChanged = onTitleChanged
            )
        }
        composable<PredatorTypesNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) {
            PredatorTypeListScreen(
                viewModel = predatorTypeViewModel,
                navController = navController,
                onTitleChanged = onTitleChanged
            )
        }
        composable<PredatorTypeDetailsNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<PredatorTypeDetailsNav>()
            PredatorTypeDetailsScreen(
                predatorTypeViewModel,
                entry.predatorName,
                onTitleChanged = onTitleChanged
            )
        }
        composable<ClansNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) {
            ClanListScreen(
                viewModel = clanViewModel,
                navController = navController,
                onTitleChanged = onTitleChanged
            )
        }
        composable<ClanDetailsNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<ClanDetailsNav>()
            ClanDetailScreen(
                clanViewModel = clanViewModel,
                clanName = entry.clanName,
                onTitleChanged = onTitleChanged
            )
        }
        composable<LoreNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) {
            LoreListScreen(
                viewModel = loreViewModel,
                navController = navController,
                onTitleChanged = onTitleChanged
            )
        }
        composable<LoreDetailsNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<LoreDetailsNav>()
            LoreDetailsScreen(
                loreViewModel = loreViewModel,
                navController = navController,
                title = entry.title,
                onTitleChanged = onTitleChanged
            )
        }
        composable<BackgroundDetailsNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<BackgroundDetailsNav>()
            BackgroundDetailsScreen(
                backgroundViewModel = backgroundViewModel,
                name = entry.name,
                id = entry.id,
                onTitleChanged = onTitleChanged
            )
        }


        composable<SubLoreNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<SubLoreNav>()
            SubLoreDetail(
                loreViewModel = loreViewModel,
                chapterTitle = entry.title,
                sectionTitle = entry.section,
                onTitleChanged = onTitleChanged
            )
        }
        composable<KindredNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) {
            KindredListScreen(
                viewModel = kindredViewModel, navController = navController,
                onTitleChanged = onTitleChanged
            )
        }

        composable<KindredDetailsNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<LoreDetailsNav>()
            KindredDetailsScreen(
                kindredViewModel = kindredViewModel,
                navController = navController,
                title = entry.title,
                onTitleChanged = onTitleChanged
            )
        }

        composable<SubKindredNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<SubKindredNav>()
            SubKindredDetail(
                kindredViewModel = kindredViewModel,
                chapterTitle = entry.title,
                sectionTitle = entry.section,
                onTitleChanged = onTitleChanged
            )
        }
        composable<PgNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) {
            PgListScreen(
                viewModel = pgViewModel, navController = navController,
                onTitleChanged = onTitleChanged
            )
        }

        composable<PgDetailsNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<LoreDetailsNav>()
            PgDetailsScreen(
                pgViewModel = pgViewModel,
                navController = navController,
                title = entry.title,
                onTitleChanged = onTitleChanged
            )
        }

        composable<SubPgNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<SubKindredNav>()
            SubPgDetail(
                pgViewModel = pgViewModel,
                chapterTitle = entry.title,
                sectionTitle = entry.section,
                onTitleChanged = onTitleChanged
            )
        }

        composable<NPCGeneratorNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) {
            NPCGeneratorScreen(
                modifier = Modifier,
                viewModel = npcGeneratorViewModel,
                navController = navController,
                onTitleChanged = onTitleChanged
            )
        }

        composable<RulesNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) {
            RuleListScreen(
                viewModel = rulesViewModel,
                navController = navController,
                onTitleChanged = onTitleChanged
            )
        }
        composable<RulesDetailsNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<RulesDetailsNav>()
            RulesDetailsScreen(
                rulesViewModel = rulesViewModel,
                navController = navController,
                title = entry.title,
                onTitleChanged = onTitleChanged
            )
        }
        composable<SubRuleNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<SubRuleNav>()
            SubRuleDetail(
                rulesViewModel = rulesViewModel,
                chapterTitle = entry.title,
                sectionTitle = entry.section,
                onTitleChanged = onTitleChanged
            )
        }
        composable<LoresheetNav> {
            LoresheetScreen(
                loresheetViewModel = loresheetViewModel,
                navController = navController,
                onTitleChanged = onTitleChanged
            )
        }
        composable<LoresheetDetailsNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<LoresheetDetailsNav>()
            LoresheetDetailsScreen(
                id = entry.id,
                name = entry.name,
                loresheetViewModel = loresheetViewModel,
                onTitleChanged = onTitleChanged
            )

        }
        composable<CharacterSheetCreationNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) {
            CharacterSheetScreen(
                viewModel = hiltViewModel<CharacterSheetViewModel>(),
                navController = navController,
                onTitleChanged = onTitleChanged
            )
        }
        composable<CharacterSheetEditNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<CharacterSheetEditNav>()
            CharacterSheetScreen(
                viewModel = hiltViewModel<CharacterSheetViewModel>(),
                navController = navController,
                id = entry.id,
                onTitleChanged = onTitleChanged
            )
        }
        composable<CharacterSheetVisualizationNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<CharacterSheetVisualizationNav>()
            CharacterSheetScreenVisualization(
                viewModel = hiltViewModel<CharacterSheetViewModel>(),
                navController = navController,
                id = entry.id,
                onTitleChanged = onTitleChanged
            )
        }
        composable<CharacterSheetListNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) {
            CharacterSheetListScreen(
                navController = navController,
                onTitleChanged = onTitleChanged
            )
        }

        composable<UserProfileNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }
        ) {
            UserProfileScreen(
                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(LoginNav)
                },
                onTitleChanged = onTitleChanged,
                onNavigateToFriendRequests = {
                    navController.navigate(FriendRequestsNav)
                }
            )
        }

        composable<FriendRequestsNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }
        ) {
            FriendRequestsScreen(
                onTitleChanged = onTitleChanged
            )
        }

    }
}
