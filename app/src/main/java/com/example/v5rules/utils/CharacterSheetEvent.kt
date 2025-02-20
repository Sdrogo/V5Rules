package com.example.v5rules.utils

import com.example.v5rules.data.Clan
import com.example.v5rules.data.Discipline
import com.example.v5rules.data.DisciplinePower


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
    data class AbilitySpecializationChanged(val abilityName: String, val specialization: String?) : CharacterSheetEvent() //Potresti avere bisogno di questo.

    //... Eventi per Discipline, Advantages, Backgrounds, Loresheets...
    data class DisciplineChanged(val discipline: Discipline): CharacterSheetEvent()
    data class DisciplinePowerAdded(val disciplineName: String, val power: DisciplinePower) : CharacterSheetEvent()
    data class DisciplinePowerRemoved(val disciplineName: String, val power: DisciplinePower) : CharacterSheetEvent()
    data class DisciplineLevelChanged(val disciplineName: String, val newLevel: Int) : CharacterSheetEvent() // Per cambiare il livello

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