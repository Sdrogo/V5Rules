package com.example.v5rules.utils

import com.example.v5rules.data.Advantage
import com.example.v5rules.data.Background
import com.example.v5rules.data.Clan
import com.example.v5rules.data.Discipline
import com.example.v5rules.data.DisciplinePower
import com.example.v5rules.data.Loresheet
import com.example.v5rules.data.PredatorType


// Eventi
sealed class CharacterSheetEvent {
    data class NameChanged(val name: String): CharacterSheetEvent()
    data class ClanChanged(val clan: Clan?): CharacterSheetEvent()
    data class GenerationChanged(val generation: Int): CharacterSheetEvent()
    data class SireChanged(val sire: String): CharacterSheetEvent()
    data class ConceptChanged(val concept: String): CharacterSheetEvent()
    data class AmbitionChanged(val ambition: String): CharacterSheetEvent()
    data class DesireChanged(val desire: String): CharacterSheetEvent()

    // Attributes
    data class StrengthChanged(val strength: Int): CharacterSheetEvent()
    data class DexterityChanged(val dexterity: Int): CharacterSheetEvent()
    data class StaminaChanged(val stamina: Int): CharacterSheetEvent()
    data class CharismaChanged(val charisma: Int): CharacterSheetEvent()
    data class ManipulationChanged(val manipulation: Int): CharacterSheetEvent()
    data class ComposureChanged(val composure: Int): CharacterSheetEvent()
    data class IntelligenceChanged(val intelligence: Int): CharacterSheetEvent()
    data class WitsChanged(val wits: Int): CharacterSheetEvent()
    data class ResolveChanged(val resolve: Int): CharacterSheetEvent()

    data class AbilityChanged(val abilityName: String, val level: Int) : CharacterSheetEvent()
    data class AbilitySpecializationChanged(val abilityName: String, val specialization: String) : CharacterSheetEvent() //Potresti avere bisogno di questo.

    //... Eventi per Discipline, Advantages, Backgrounds, Loresheets...
    data class DisciplineChanged(val discipline: Discipline): CharacterSheetEvent()
    data class DisciplinePowerAdded(val disciplineName: String, val power: DisciplinePower) : CharacterSheetEvent()
    data class DisciplinePowerRemoved(val disciplineName: String, val power: DisciplinePower) : CharacterSheetEvent()
    data class DisciplineLevelChanged(val disciplineName: String, val newLevel: Int) : CharacterSheetEvent() // Per cambiare il livello

    // Health

    data class HealthBoxClicked(val index: Int): CharacterSheetEvent()

    // Willpower
    data class WillpowerBoxClicked(val index: Int): CharacterSheetEvent()

    // Humanity
    data class HumanityChanged(val current: Int): CharacterSheetEvent()
    data class StainsChanged(val stains: Int): CharacterSheetEvent()

    // Experience
    data class TotalExperienceChanged(val total: Int): CharacterSheetEvent()
    data class SpentExperienceChanged(val spent: Int): CharacterSheetEvent()

    //BACKGROUNDS section
    data class LoresheetAdded(val loresheet: Loresheet, val level: Int) : CharacterSheetEvent()
    data class LoresheetRemoved(val loresheet: Loresheet) : CharacterSheetEvent()
    data class LoresheetLevelChanged(val loresheetName: String, val level: Int) : CharacterSheetEvent()
    data class BackgroundAdded(val background: Background, val level: Int) : CharacterSheetEvent()
    data class BackgroundRemoved(val background: Background) : CharacterSheetEvent()
    data class BackgroundLevelChanged(val background: Background, val level: Int) : CharacterSheetEvent()
    data class AdvantageAdded(val advantage: Advantage, val background: Background, val level: Int) : CharacterSheetEvent()
    data class AdvantageRemoved(val advantage: Advantage, val background: Background) : CharacterSheetEvent()
    data class AdvantageLevelChanged(val advantage: Advantage, val background: Background, val level: Int) : CharacterSheetEvent()
    data class AdvantageFlawAdded(val advantage: Advantage, val background: Background, val level: Int) : CharacterSheetEvent()
    data class AdvantageFlawRemoved(val advantage: Advantage, val background: Background) : CharacterSheetEvent()
    data class AdvantageFlawLevelChanged(val advantage: Advantage, val background: Background, val level: Int) : CharacterSheetEvent()
    class PredatorChanged(val predator: PredatorType) : CharacterSheetEvent()
    // Eventi per Merits/Flaws di un Background specifico
    data class BackgroundMeritAdded(val background: Background, val merit: Advantage, val level: Int) : CharacterSheetEvent()
    data class BackgroundMeritRemoved(val background: Background, val merit: Advantage) : CharacterSheetEvent()
    data class BackgroundMeritLevelChanged(val background: Background, val meritId: Int, val newLevel: Int) : CharacterSheetEvent()

    data class BackgroundFlawAdded(val background: Background, val flaw: Advantage, val level: Int) : CharacterSheetEvent()
    data class BackgroundFlawRemoved(val background: Background, val flaw: Advantage) : CharacterSheetEvent()
    data class BackgroundFlawLevelChanged(val background: Background, val flaw: Advantage, val newLevel: Int) : CharacterSheetEvent()

    // Eventi per DirectFlaws del personaggio (se li tieni separati)
    data class CharacterDirectFlawAdded(val directFlaw: Advantage, val level: Int) : CharacterSheetEvent()
    data class CharacterDirectFlawRemoved(val directFlaw: Advantage) : CharacterSheetEvent()
    data class CharacterDirectFlawLevelChanged(val directFlaw: Advantage, val newLevel: Int) : CharacterSheetEvent()

    data class AddNoteToBackground(val background: Background, val note: String) : CharacterSheetEvent()
    data class AddNoteToBackgroundFlaw(val background: Background, val flaw: Advantage, val note: String) : CharacterSheetEvent()
    data class AddNoteToDirectFlaw(val advantage: Advantage, val note: String) : CharacterSheetEvent()
    data class AddNoteToMerit(val background: Background,  val merit: Advantage,val note: String) : CharacterSheetEvent()
    data class AddNoteToFlaw(val background: Background,  val flaw: Advantage,val note: String) : CharacterSheetEvent()
    data class RemoveNoteToBackground(val background: Background) : CharacterSheetEvent()
    data class RemoveNoteToBackgroundFlaw(val background: Background, val flaw: Advantage) : CharacterSheetEvent()
    data class RemoveNoteToDirectFlaw(val advantage: Advantage) : CharacterSheetEvent()
    data class RemoveNoteToMerit(val background: Background,  val merit: Advantage) : CharacterSheetEvent()
    data class RemoveNoteToFlaw(val background: Background,  val flaw: Advantage) : CharacterSheetEvent()
    data class HungerChanged(val newHunger:Int) : CharacterSheetEvent()

    // Azioni
    object SaveClicked: CharacterSheetEvent()
    object DeleteClicked: CharacterSheetEvent()
    object CleanupClicked: CharacterSheetEvent()

}