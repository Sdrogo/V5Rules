package com.example.v5rules.utils

import com.example.v5rules.data.Clan


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

    // Abilities (Physical)
    data class AthleticsChanged(val athletics: Int): CharacterSheetEvent()
    data class BrawlChanged(val brawl: Int): CharacterSheetEvent()
    data class CraftChanged(val craft: Int): CharacterSheetEvent()
    data class DriveChanged(val drive: Int): CharacterSheetEvent()
    data class FirearmsChanged(val firearms: Int): CharacterSheetEvent()
    data class MeleeChanged(val melee: Int): CharacterSheetEvent()
    data class LarcenyChanged(val larceny: Int): CharacterSheetEvent()
    data class StealthChanged(val stealth: Int): CharacterSheetEvent()
    data class SurvivalChanged(val survival: Int): CharacterSheetEvent()

    // Abilities (Social)
    data class AnimalKenChanged(val animalKen: Int): CharacterSheetEvent()
    data class EtiquetteChanged(val etiquette: Int): CharacterSheetEvent()
    data class InsightChanged(val insight: Int): CharacterSheetEvent()
    data class IntimidationChanged(val intimidation: Int): CharacterSheetEvent()
    data class LeadershipChanged(val leadership: Int): CharacterSheetEvent()
    data class PerformanceChanged(val performance: Int): CharacterSheetEvent()
    data class PersuasionChanged(val persuasion: Int): CharacterSheetEvent()
    data class StreetwiseChanged(val streetwise: Int): CharacterSheetEvent()
    data class SubterfugeChanged(val subterfuge: Int): CharacterSheetEvent()

    // Abilities (Mental)
    data class AcademicsChanged(val academics: Int): CharacterSheetEvent()
    data class AwarenessChanged(val awareness: Int): CharacterSheetEvent()
    data class FinanceChanged(val finance: Int): CharacterSheetEvent()
    data class InvestigationChanged(val investigation: Int): CharacterSheetEvent()
    data class MedicineChanged(val medicine: Int): CharacterSheetEvent()
    data class OccultChanged(val occult: Int): CharacterSheetEvent()
    data class PoliticsChanged(val politics: Int): CharacterSheetEvent()
    data class ScienceChanged(val science: Int): CharacterSheetEvent()
    data class TechnologyChanged(val technology: Int): CharacterSheetEvent()

    //... Eventi per Discipline, Advantages, Backgrounds, Loresheets...

    // Health
    data class MaxHealthChanged(val max: Int): CharacterSheetEvent()
    data class CurrentHealthChanged(val current: Int): CharacterSheetEvent()

    // Willpower
    data class MaxWillpowerChanged(val max: Int): CharacterSheetEvent()
    data class CurrentWillpowerChanged(val current: Int): CharacterSheetEvent()

    // Humanity
    data class HumanityChanged(val current: Int): CharacterSheetEvent()
    data class StainsChanged(val stains: Int): CharacterSheetEvent()

    // Experience
    data class TotalExperienceChanged(val total: Int): CharacterSheetEvent()
    data class SpentExperienceChanged(val spent: Int): CharacterSheetEvent()

    // Azioni
    object SaveClicked: CharacterSheetEvent()
    object DeleteClicked: CharacterSheetEvent()
    object CleanupClicked: CharacterSheetEvent()

}