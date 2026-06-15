package com.example.v5rules.data

import kotlinx.serialization.Serializable
import java.util.UUID
import kotlin.collections.plus

@Serializable
data class Character(
    var id: String = "",
    val ownerId: String = "",
    val sharedWith: List<String> = emptyList(),
    val name: String = "",
    val clan: Clan? = null,
    val generation: Int = 12,
    val sire: String = "",
    val concept: String = "",
    val ambition: String = "",
    val desire: String = "",
    val predator: PredatorType? = null,
    val attributes: Attributes = Attributes(),
    val abilities: List<Ability> = emptyList(),
    val disciplines: List<Discipline> = emptyList(),
    val advantages: List<Background> = emptyList(),
    val backgrounds: List<Background> = emptyList(),
    val directFlaws: List<Advantage> = emptyList(),
    val loresheets: List<Loresheet> = emptyList(),
    val health: Health = Health(),
    val willpower: Willpower = Willpower(),
    val humanity: Humanity = Humanity(),
    val experience: Experience = Experience(),
    val hunger: Int = 1,
    val learnedRituals: List<Ritual> = emptyList()
)

@Serializable
data class Attributes(
    val strength: Int = 1,
    val dexterity: Int = 1,
    val stamina: Int = 1,
    val charisma: Int = 1,
    val manipulation: Int = 1,
    val composure: Int = 1,
    val intelligence: Int = 1,
    val wits: Int = 1,
    val resolve: Int = 1
)

@Serializable
data class Ability(
    val id: Int = 0,
    val name: String = "",
    val level: Int = 0,
    val specialization: String? = null
)

@Serializable
data class Health(
    val boxes: List<DamageType> = List(10) { DamageType.EMPTY }
)

@Serializable
data class Willpower(
    val boxes: List<DamageType> = List(10) { DamageType.EMPTY }
)

@Serializable
data class Humanity(
    val current: Int = 7,
    var stains: Int = 0
)

@Serializable
data class Experience(
    val total: Int = 0,
    val spent: Int = 0
)

enum class DamageType {
    EMPTY,
    SUPERFICIAL,
    AGGRAVATED
}

// --- DOMAIN LOGIC ---

fun Character.updateName(newName: String): Character = this.copy(name = newName)
fun Character.updateClan(newClan: Clan?): Character = this.copy(clan = newClan)
fun Character.updatePredator(newPredator: PredatorType?): Character =
    this.copy(predator = newPredator)

fun Character.updateGeneration(newGen: Int): Character = this.copy(generation = newGen)
fun Character.updateSire(newSire: String): Character = this.copy(sire = newSire)
fun Character.updateConcept(newConcept: String): Character = this.copy(concept = newConcept)
fun Character.updateAmbition(newAmbition: String): Character = this.copy(ambition = newAmbition)
fun Character.updateDesire(newDesire: String): Character = this.copy(desire = newDesire)

fun Character.updateStrength(level: Int): Character =
    this.copy(attributes = attributes.copy(strength = level))

fun Character.updateDexterity(level: Int): Character =
    this.copy(attributes = attributes.copy(dexterity = level))

fun Character.updateCharisma(level: Int): Character =
    this.copy(attributes = attributes.copy(charisma = level))

fun Character.updateManipulation(level: Int): Character =
    this.copy(attributes = attributes.copy(manipulation = level))

fun Character.updateIntelligence(level: Int): Character =
    this.copy(attributes = attributes.copy(intelligence = level))

fun Character.updateWits(level: Int): Character =
    this.copy(attributes = attributes.copy(wits = level))

fun Character.updateStamina(newStamina: Int): Character {
    val newMaxHealth = newStamina + 3
    return this.copy(
        attributes = this.attributes.copy(stamina = newStamina),
        health = this.health.copy(boxes = synchronizeDamageTrack(this.health.boxes, newMaxHealth))
    )
}

fun Character.updateComposure(newComposure: Int): Character {
    val newMaxWillpower = newComposure + this.attributes.resolve
    return this.copy(
        attributes = this.attributes.copy(composure = newComposure),
        willpower = this.willpower.copy(
            boxes = synchronizeDamageTrack(
                this.willpower.boxes,
                newMaxWillpower
            )
        )
    )
}

fun Character.updateResolve(newResolve: Int): Character {
    val newMaxWillpower = newResolve + this.attributes.composure
    return this.copy(
        attributes = this.attributes.copy(resolve = newResolve),
        willpower = this.willpower.copy(
            boxes = synchronizeDamageTrack(
                this.willpower.boxes,
                newMaxWillpower
            )
        )
    )
}

fun Character.updateAbilityLevel(abilityName: String, level: Int): Character {
    val updatedAbilities = if (this.abilities.any { it.name == abilityName }) {
        this.abilities.map { if (it.name == abilityName) it.copy(level = level) else it }
    } else {
        this.abilities + Ability(name = abilityName, level = level)
    }
    return this.copy(abilities = updatedAbilities)
}

fun Character.updateAbilitySpecialization(abilityName: String, specialization: String?): Character {
    val updatedAbilities = if (this.abilities.any { it.name == abilityName }) {
        this.abilities.map { if (it.name == abilityName) it.copy(specialization = specialization) else it }
    } else {
        this.abilities + Ability(name = abilityName, specialization = specialization)
    }
    return this.copy(abilities = updatedAbilities)
}

fun Character.toggleHealthBox(index: Int): Character {
    val currentBoxes = this.health.boxes.toMutableList()
    if (index < currentBoxes.size) {
        currentBoxes[index] = when (currentBoxes[index]) {
            DamageType.EMPTY -> DamageType.SUPERFICIAL
            DamageType.SUPERFICIAL -> DamageType.AGGRAVATED
            DamageType.AGGRAVATED -> DamageType.EMPTY
        }
        return this.copy(health = this.health.copy(boxes = currentBoxes))
    }
    return this
}

fun Character.toggleWillpowerBox(index: Int): Character {
    val currentBoxes = this.willpower.boxes.toMutableList()
    if (index < currentBoxes.size) {
        currentBoxes[index] = when (currentBoxes[index]) {
            DamageType.EMPTY -> DamageType.SUPERFICIAL
            DamageType.SUPERFICIAL -> DamageType.AGGRAVATED
            DamageType.AGGRAVATED -> DamageType.EMPTY
        }
        return this.copy(willpower = this.willpower.copy(boxes = currentBoxes))
    }
    return this
}

fun Character.updateHunger(newHunger: Int): Character = this.copy(hunger = newHunger.coerceIn(0, 5))
fun Character.updateHumanity(newHumanity: Int): Character =
    this.copy(humanity = this.humanity.copy(current = newHumanity.coerceIn(0, 10)))

fun Character.updateStains(newStains: Int): Character =
    this.copy(humanity = this.humanity.copy(stains = newStains.coerceAtLeast(0)))

fun Character.updateTotalExperience(total: Int): Character =
    this.copy(experience = experience.copy(total = total))

fun Character.updateSpentExperience(spent: Int): Character =
    this.copy(experience = experience.copy(spent = spent))

// --- DISCIPLINE LOGIC ---

fun Character.addDiscipline(discipline: Discipline): Character {
    if (disciplines.any { it.title == discipline.title }) return this
    val newDiscipline = discipline.copy(level = 1)
    var updatedCharacter = this.copy(disciplines = disciplines + newDiscipline)

    if (newDiscipline.id == "d9" || newDiscipline.id == "d10") {
        newDiscipline.ritual?.let {
            updatedCharacter = updatedCharacter.addRitual(newDiscipline.title, it)
        }
    }
    return updatedCharacter
}

fun Character.updateDisciplineLevel(disciplineTitle: String, newLevel: Int): Character {
    if (newLevel == 0) {
        return this.copy(
            disciplines = disciplines.filterNot { it.title == disciplineTitle },
            learnedRituals = learnedRituals.filterNot { it.title == disciplineTitle }
        )
    }
    val updatedDisciplines = disciplines.map {
        if (it.title == disciplineTitle) {
            val validPowers = it.selectedDisciplinePowers.filter { p -> p.level <= newLevel }
            it.copy(level = newLevel, selectedDisciplinePowers = validPowers)
        } else it
    }
    return this.copy(disciplines = updatedDisciplines)
}

fun Character.addDisciplinePower(disciplineName: String, power: DisciplinePower): Character {
    val updatedDisciplines = disciplines.map {
        if (it.title == disciplineName && it.selectedDisciplinePowers.none { p -> p.id == power.id }) {
            it.copy(selectedDisciplinePowers = it.selectedDisciplinePowers + power)
        } else it
    }
    return this.copy(disciplines = updatedDisciplines.toList())
}

fun Character.removeDisciplinePower(disciplineName: String, power: DisciplinePower): Character {
    val updatedDisciplines = disciplines.map {
        if (it.title == disciplineName) {
            it.copy(selectedDisciplinePowers = it.selectedDisciplinePowers.filterNot { p -> p.id == power.id })
        } else it
    }
    return this.copy(disciplines = updatedDisciplines.toList())
}

// --- RITUAL LOGIC ---

fun Character.addRitual(disciplineName: String, ritual: Ritual): Character {
    val discipline = disciplines.find { it.title == disciplineName } ?: return this
    if (discipline.level < 1 || learnedRituals.any { it.id == ritual.id }) return this
    return this.copy(
        learnedRituals = learnedRituals + ritual.copy(
            level = 1,
            ritualsPowers = emptyList()
        )
    )
}

fun Character.removeRitual(ritualId: String): Character =
    this.copy(learnedRituals = learnedRituals.filterNot { it.id == ritualId })

fun Character.updateRitualLevel(ritualTitle: String, level: Int): Character {
    val updatedRituals =
        learnedRituals.map { if (it.title == ritualTitle) it.copy(level = level) else it }
    return this.copy(learnedRituals = updatedRituals.toList())
}

fun Character.addRitualPower(ritualTitle: String, power: RitualPower): Character {
    val updatedRituals = learnedRituals.map {
        if (it.title == ritualTitle && it.ritualsPowers.none { p -> p.id == power.id }) {
            it.copy(ritualsPowers = it.ritualsPowers + power)
        } else it
    }
    return this.copy(learnedRituals = updatedRituals.toList())
}

fun Character.removeRitualPower(ritualTitle: String, powerId: String): Character {
    val updatedRituals = learnedRituals.map {
        if (it.title == ritualTitle) {
            it.copy(ritualsPowers = it.ritualsPowers.filterNot { p -> p.id == powerId })
        } else it
    }
    return this.copy(learnedRituals = updatedRituals.toList())
}

// --- BACKGROUNDS & ADVANTAGES ---

fun Character.addBackground(background: Background, level: Int = 1): Character {
    val newBg = background.copy(
        level = level,
        identifier = UUID.randomUUID().toString(),
        directFlaws = emptyList(),
        merits = emptyList(),
        flaws = emptyList()
    )
    return this.copy(backgrounds = backgrounds + newBg)
}

fun Character.removeBackground(identifier: String): Character =
    this.copy(backgrounds = backgrounds.filterNot { it.identifier == identifier })

fun Character.updateBackgroundLevel(identifier: String, level: Int): Character {
    return this.copy(backgrounds = backgrounds.map { if (it.identifier == identifier) it.copy(level = level) else it })
}

fun Character.addBackgroundMerit(backgroundId: String, merit: Advantage, level: Int): Character {
    return this.copy(backgrounds = backgrounds.map {
        if (it.identifier == backgroundId) {
            it.copy(
                merits = it.merits + merit.copy(
                    identifier = UUID.randomUUID().toString(),
                    level = level
                )
            )
        } else it
    })
}

fun Character.removeBackgroundMerit(backgroundId: String, meritId: String): Character {
    return this.copy(backgrounds = backgrounds.map {
        if (it.identifier == backgroundId) it.copy(merits = it.merits.filterNot { m -> m.identifier == meritId }) else it
    })
}

fun Character.updateBackgroundMeritLevel(
    backgroundId: String,
    meritId: String,
    level: Int
): Character {
    return this.copy(backgrounds = backgrounds.map {
        if (it.identifier == backgroundId) {
            it.copy(merits = it.merits.map { m -> if (m.identifier == meritId) m.copy(level = level) else m })
        } else it
    })
}

fun Character.addBackgroundFlaw(backgroundId: String, flaw: Advantage, level: Int): Character {
    return this.copy(backgrounds = backgrounds.map {
        if (it.identifier == backgroundId) {
            it.copy(
                flaws = it.flaws + flaw.copy(
                    identifier = UUID.randomUUID().toString(),
                    level = level
                )
            )
        } else it
    })
}

fun Character.removeBackgroundFlaw(backgroundId: String, flawId: String): Character {
    return this.copy(backgrounds = backgrounds.map {
        if (it.identifier == backgroundId) it.copy(flaws = it.flaws.filterNot { f -> f.identifier == flawId }) else it
    })
}

fun Character.updateBackgroundFlawLevel(
    backgroundId: String,
    flawId: String,
    level: Int
): Character {
    return this.copy(backgrounds = backgrounds.map {
        if (it.identifier == backgroundId) {
            it.copy(flaws = it.flaws.map { f -> if (f.identifier == flawId) f.copy(level = level) else f })
        } else it
    })
}

// --- DIRECT FLAWS ---

fun Character.addDirectFlaw(flaw: Advantage, level: Int): Character {
    return this.copy(
        directFlaws = directFlaws + flaw.copy(
            identifier = UUID.randomUUID().toString(), level = level
        )
    )
}

fun Character.removeDirectFlaw(identifier: String): Character =
    this.copy(directFlaws = directFlaws.filterNot { it.identifier == identifier })

fun Character.updateDirectFlawLevel(identifier: String, level: Int): Character {
    return this.copy(directFlaws = directFlaws.map { if (it.identifier == identifier) it.copy(level = level) else it })
}

// --- NOTES LOGIC ---

fun Character.updateBackgroundNote(backgroundId: String, note: String?): Character {
    return this.copy(backgrounds = backgrounds.map { if (it.identifier == backgroundId) it.copy(note = note) else it })
}

fun Character.updateMeritNote(backgroundId: String, meritId: String, note: String?): Character {
    return this.copy(backgrounds = backgrounds.map {
        if (it.identifier == backgroundId) {
            it.copy(merits = it.merits.map { m -> if (m.identifier == meritId) m.copy(note = note) else m })
        } else it
    })
}

fun Character.updateFlawNote(backgroundId: String, flawId: String, note: String?): Character {
    return this.copy(backgrounds = backgrounds.map {
        if (it.identifier == backgroundId) {
            it.copy(flaws = it.flaws.map { f -> if (f.identifier == flawId) f.copy(note = note) else f })
        } else it
    })
}
fun Character.updateDirectFlawNote(identifier: String, note: String?): Character {
    return this.copy(directFlaws = directFlaws.map { if (it.identifier == identifier) it.copy(note = note) else it })
}

// --- ADVANTAGE LOGIC ---
fun Character.updateAdvantage(advantage: Advantage, background: Background, level: Int): Character {
    val backgrounds = this.backgrounds.toMutableList()
    val backgroundIndex =
        backgrounds.indexOfFirst { it.title == background.title }
    if (backgroundIndex != -1) {
        val advantages =
            if (advantage.isFlaw == true)
                backgrounds[backgroundIndex].flaws.toMutableList()
            else
                backgrounds[backgroundIndex].merits
                    .toMutableList()
        val updatedVantage =
            advantages.find {
                it.id == advantage.id
            }?.copy(level = level)
        if (updatedVantage != null) {
            advantages[advantages.indexOf(advantage)] = updatedVantage
        }
        backgrounds[backgroundIndex] =
            if (advantage.isFlaw == true)
                backgrounds[backgroundIndex].copy(flaws = advantages)
            else
                backgrounds[backgroundIndex].copy(merits = advantages)
    }
    return this.copy(backgrounds = backgrounds)
}

fun Character.addAdvantageFlaw(
    advantage: Advantage,
    background: Background,
    level: Int
): Character {
    val backgrounds = this.backgrounds.toMutableList()
    val backgroundIndex =
        backgrounds.indexOfFirst { it.title == background.title }
    if (backgroundIndex != -1) {
        val currentBackground = backgrounds[backgroundIndex]
        val currentAdvanges = currentBackground.flaws
        val updatedAdvantages =
            currentAdvanges + advantage.copy(level = level)
        val updatedBackground =
            currentBackground.copy(flaws = updatedAdvantages)
        backgrounds[backgroundIndex] = updatedBackground
    }
    return this.copy(backgrounds = backgrounds)
}

fun Character.removeAdvantageFlaw(advantage: Advantage, background: Background): Character {
    val backgrounds = this.backgrounds.toMutableList()
    val backgroundIndex =
        backgrounds.indexOfFirst { it.title == background.title }
    if (backgroundIndex != -1) {
        val currentBackground = backgrounds[backgroundIndex]
        val currentAdvanges = currentBackground.merits
        val updatedAdvantages = currentAdvanges - advantage
        val updatedBackground =
            currentBackground.copy(merits = updatedAdvantages)
        backgrounds[backgroundIndex] = updatedBackground

    }
    return this.copy(backgrounds = backgrounds)
}

fun Character.updateAdvantageFlawLevel(
    advantage: Advantage,
    background: Background,
    level: Int
): Character {
    val backgrounds = this.backgrounds.toMutableList()
    val backgroundIndex =
        backgrounds.indexOfFirst { it.title == background.title }
    if (backgroundIndex != -1) {
        val advantages =
            backgrounds[backgroundIndex].merits.toMutableList()
        val updatedVantage =
            backgrounds[backgroundIndex].merits.find {
                it.id == advantage.id
            }?.copy(level = level)
        if (updatedVantage != null) {
            advantages[advantages.indexOf(advantage)] = updatedVantage
        }
        backgrounds[backgroundIndex] =
            backgrounds[backgroundIndex].copy(merits = advantages)

    }
    return this.copy(backgrounds = backgrounds)
}

fun Character.removeAdvantage(advantage: Advantage, background: Background): Character {
    val backgrounds = this.backgrounds.toMutableList()
    val backgroundIndex = backgrounds.indexOfFirst { it.title == background.title }
    if (backgroundIndex != -1) {
        val currentBackground = backgrounds[backgroundIndex]
        val currentAdvanges =
            if (advantage.isFlaw == true)
                currentBackground.flaws
            else
                currentBackground.merits
        val updatedAdvantages = currentAdvanges - advantage
        val updatedBackground =
            if (advantage.isFlaw == true)
                currentBackground.copy(flaws = updatedAdvantages)
            else
                currentBackground.copy(merits = updatedAdvantages)
        backgrounds[backgroundIndex] = updatedBackground
    }
    return this.copy(backgrounds = backgrounds)
}

fun Character.addAdvantage(advantage: Advantage, background: Background, level: Int): Character {
    val backgrounds = this.backgrounds.toMutableList()
    val backgroundIndex =
        backgrounds.indexOfFirst { it.title == background.title }
    if (backgroundIndex != -1) {
        val currentBackground = backgrounds[backgroundIndex]
        val currentAdvanges =
            if (advantage.isFlaw == true)
                currentBackground.flaws
            else
                currentBackground.merits
        val updatedAdvantages =
            currentAdvanges + advantage.copy(level = level)
        val updatedBackground =
            if (advantage.isFlaw == true)
                currentBackground.copy(flaws = updatedAdvantages)
            else
                currentBackground.copy(merits = updatedAdvantages)
        backgrounds[backgroundIndex] = updatedBackground
    }
    return this.copy(backgrounds = backgrounds)

}

// --- LORESHEETS ---

fun Character.addLoresheet(loresheet: Loresheet, level: Int): Character {
    if (loresheets.any { it.title == loresheet.title }) return this
    return this.copy(loresheets = loresheets + loresheet.copy(level = level))
}

fun Character.removeLoresheet(title: String): Character =
    this.copy(loresheets = loresheets.filterNot { it.title == title })

fun Character.updateLoresheetLevel(title: String, level: Int): Character {
    return this.copy(loresheets = loresheets.map { if (it.title == title) it.copy(level = level) else it })
}

private fun synchronizeDamageTrack(currentBoxes: List<DamageType>, newSize: Int): List<DamageType> {
    val newBoxes = MutableList(newSize) { DamageType.EMPTY }
    for (i in 0 until minOf(currentBoxes.size, newSize)) {
        newBoxes[i] = currentBoxes[i]
    }
    return newBoxes.toList()
}
