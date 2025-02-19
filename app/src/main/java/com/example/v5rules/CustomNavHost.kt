package com.example.v5rules

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.v5rules.ui.compose.screen.HomeScreen
import com.example.v5rules.ui.compose.screen.NPCGeneratorScreen
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
import kotlinx.serialization.Serializable

@Serializable
object HomeNav

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
object CharacterSheetListNav

@Serializable
object CharacterSheetCreationNav

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
data class LoresheetDetailsNav(val name: String, val id: Int)

@Serializable
data class CharacterSheetEditNav(val id: Int)

@Composable
fun CustomNavHost(
    navController: NavHostController,
    disciplineViewModel: DisciplineViewModel,
    clanViewModel: ClanViewModel,
    predatorTypeViewModel: PredatorTypeViewModel,
    rulesViewModel: RulesViewModel,
    loreViewModel: LoreViewModel,
    loresheetViewModel: LoresheetViewModel,
    npcGeneratorViewModel: NPCGeneratorViewModel,
    kindredViewModel: KindredViewModel,
    pgViewModel: PgViewModel,
    characterSheetViewModel: CharacterSheetViewModel
) {
    NavHost(navController = navController, startDestination = HomeNav) {
        val enterTransition = fadeIn(
            animationSpec = tween(
                durationMillis = 500, // Duration of the animation
                delayMillis = 100, // Delay before the animation starts
                easing = LinearOutSlowInEasing // Easing curve for the animation
            ),
            initialAlpha = 0.0f
        )
        val exitTransition = fadeOut(
            animationSpec = tween(
                durationMillis = 300,
                delayMillis = 0,
                easing = FastOutLinearInEasing
            ),
            targetAlpha = 1f
        )

        composable<HomeNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition })
        {
            HomeScreen(navController)
        }
        composable<DisciplinesNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) {
            DisciplineScreen(disciplineViewModel, navController)
        }
        composable<DisciplineDetailsNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<DisciplineDetailsNav>()
            DisciplineDetailScreen(
                disciplineId = entry.disciplineId,
                disciplineViewModel,
                navController
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
                navController = navController
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
                navController = navController
            )
        }
        composable<PredatorTypesNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) {
            PredatorTypeListScreen(
                viewModel = predatorTypeViewModel,
                navController = navController
            )
        }
        composable<PredatorTypeDetailsNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<PredatorTypeDetailsNav>()
            PredatorTypeDetailsScreen(
                predatorTypeViewModel,
                navController,
                entry.predatorName
            )
        }
        composable<ClansNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) {
            ClanListScreen(viewModel = clanViewModel, navController = navController)
        }
        composable<ClanDetailsNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<ClanDetailsNav>()
            ClanDetailScreen(
                clanViewModel = clanViewModel,
                navController = navController,
                clanName = entry.clanName
            )
        }
        composable<LoreNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) {
            LoreListScreen(
                loreViewModel = loreViewModel, navController = navController
            )
        }
        composable<LoreDetailsNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<LoreDetailsNav>()
            LoreDetailsScreen(
                loreViewModel = loreViewModel,
                navController = navController,
                title = entry.title
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
                navController = navController
            )
        }
        composable<KindredNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) {
            KindredListScreen(
                kindredViewModel = kindredViewModel, navController = navController
            )
        }

        composable<KindredDetailsNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<LoreDetailsNav>()
            KindredDetailsScreen(
                kindredViewModel = kindredViewModel,
                navController = navController,
                title = entry.title
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
                navController = navController
            )
        }
        composable<PgNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) {
            PgListScreen(
                pgViewModel = pgViewModel, navController = navController
            )
        }

        composable<PgDetailsNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<LoreDetailsNav>()
            PgDetailsScreen(
                pgViewModel = pgViewModel,
                navController = navController,
                title = entry.title
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
                navController = navController
            )
        }

        composable<NPCGeneratorNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) {
            NPCGeneratorScreen(
                modifier = Modifier,
                viewModel = npcGeneratorViewModel,
                navController = navController
            )
        }

        composable<RulesNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) {
            RuleListScreen(viewModel = rulesViewModel, navController = navController)
        }
        composable<RulesDetailsNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<RulesDetailsNav>()
            RulesDetailsScreen(
                rulesViewModel = rulesViewModel,
                navController = navController,
                title = entry.title
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
                navController = navController
            )
        }
        composable<LoresheetNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }
        ) {
            LoresheetScreen(loresheetViewModel = loresheetViewModel, navController = navController)
        }
        composable<LoresheetDetailsNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<LoresheetDetailsNav>()
            LoresheetDetailsScreen(
                id = entry.id,
                name = entry.name,
                loresheetViewModel = loresheetViewModel,
                navController = navController
            )
        }
        composable<CharacterSheetCreationNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }){
            CharacterSheetScreen(
                viewModel = characterSheetViewModel,
                modifier = Modifier,
                navController = navController
            )
        }
        composable<CharacterSheetEditNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }) { backStackEntry ->
            val entry = backStackEntry.toRoute<CharacterSheetEditNav>()
            CharacterSheetScreen(
                viewModel = characterSheetViewModel,
                modifier = Modifier,
                navController = navController,
                id = entry.id,
            )
        }
        composable<CharacterSheetListNav>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition }){
            CharacterSheetListScreen(
                navController = navController
            )
        }
    }
}