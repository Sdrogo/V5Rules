package com.example.v5rules.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CharacterLogicTest {

    @Test
    fun `updateName returns new character with updated name`() {
        val character = Character(name = "Old")
        val updated = character.updateName("New")
        assertEquals("New", updated.name)
    }

    @Test
    fun `updateClan returns new character with updated clan`() {
        val clan = Clan(name = "Toreador")
        val character = Character(clan = null)
        val updated = character.updateClan(clan)
        assertEquals("Toreador", updated.clan?.name)
    }


    @Test
    fun `updateHumanity updates the humanity correctly`() {
        val character = Character(humanity = Humanity(7))
        val updated = character.updateHumanity(6)
        assertEquals(6, updated.humanity.current)
    }

    @Test
    fun `updateStains updates the stains correctly`() {
        val character = Character(humanity = Humanity(7, 0))
        val updated = character.updateStains(2)
        assertEquals(2, updated.humanity.stains)
    }

    @Test
    fun `updateTotalExperience updates the experience correctly`() {
        val character = Character(experience = Experience(0))
        val updated = character.updateTotalExperience(2)
        assertEquals(2, updated.experience.total)
    }

    @Test
    fun `updateSpentExperience updates the experience correctly`() {
        val character = Character(experience = Experience(5, 0))
        val updated = character.updateSpentExperience(5)
        assertEquals(5, updated.experience.spent)
    }

    @Test
    fun `updateDexterity updates the dexterity correctly`() {
        val character = Character(attributes = Attributes(dexterity = 1))
        val updated = character.updateDexterity(3)
        assertEquals(3, updated.attributes.dexterity)
    }

    @Test
    fun `updateCharisma updates the charisma correctly`() {
        val character = Character(attributes = Attributes(charisma = 1))
        val updated = character.updateCharisma(3)
        assertEquals(3, updated.attributes.charisma)
    }

    @Test
    fun `updateManipulation updates the manipulation correctly`() {
        val character = Character(attributes = Attributes(manipulation = 1))
        val updated = character.updateManipulation(3)
        assertEquals(3, updated.attributes.manipulation)
    }

    @Test
    fun `updateIntelligence updates the intelligence correctly`() {
        val character = Character(attributes = Attributes(intelligence = 1))
        val updated = character.updateIntelligence(3)
        assertEquals(3, updated.attributes.intelligence)
    }

    @Test
    fun `updateWits updates the wits correctly`() {
        val character = Character(attributes = Attributes(wits = 1))
        val updated = character.updateWits(3)
        assertEquals(3, updated.attributes.wits)
    }

    @Test
    fun `updateStamina updates health boxes correctly`() {
        val character = Character(attributes = Attributes(stamina = 1))
        // Health = stamina + 3 = 4
        assertEquals(4, character.updateStamina(1).health.boxes.size)
        
        val updated = character.updateStamina(3)
        // Health = 3 + 3 = 6
        assertEquals(3, updated.attributes.stamina)
        assertEquals(6, updated.health.boxes.size)
    }

    @Test
    fun `updateComposure updates willpower boxes correctly`() {
        val character = Character(attributes = Attributes(composure = 1, resolve = 3))
        assertEquals(4, character.updateComposure(1).willpower.boxes.size)

        val updated = character.updateComposure(3)
        assertEquals(3, updated.attributes.composure)
        assertEquals(6, updated.willpower.boxes.size)
    }

    @Test
    fun `updateResolve updates willpower boxes correctly`() {
        val character = Character(attributes = Attributes(composure = 3, resolve = 1))
        assertEquals(4, character.updateResolve(1).willpower.boxes.size)

        val updated = character.updateResolve(3)
        assertEquals(3, updated.attributes.resolve)
        assertEquals(6, updated.willpower.boxes.size)
    }

    @Test
    fun `generationChanged updates generation correctly`() {
        val character = Character(generation = 12)
        val updated = character.updateGeneration(11)
        assertEquals(11, updated.generation)
    }

    @Test
    fun `updateSire updates the Sire correctly`() {
        val character = Character(sire = "Pinco")
        val updated = character.updateSire("Panco")
        assertEquals("Panco", updated.sire)
    }

    @Test
    fun `conceptChanged updates the concept correctly`() {
        val character = Character(concept = "Blabla")
        val updated = character.updateConcept("Blablabla")
        assertEquals("Blablabla", updated.concept)
    }

    @Test
    fun `updateAmbition updates the ambition correctly`() {
        val character = Character(ambition = "Blabla")
        val updated = character.updateAmbition("Blablabla")
        assertEquals("Blablabla", updated.ambition)
    }

    @Test
    fun `desireChanged updates the desire correctly`() {
        val character = Character(desire = "Blabla")
        val updated = character.updateDesire("Blablabla")
        assertEquals("Blablabla", updated.desire)
    }

    @Test
    fun `desireChanged updates the ambition correctly`() {
        val character = Character(desire = "Blabla")
        val updated = character.updateDesire("Blablabla")
        assertEquals("Blablabla", updated.desire)
    }

    @Test
    fun `updateAbilityLevel adds or updates ability`() {
        val character = Character()
        var updated = character.updateAbilityLevel("Athletics", 3)
        assertEquals(3, updated.abilities.find { it.name == "Athletics" }?.level)

        updated = updated.updateAbilityLevel("Athletics", 5)
        assertEquals(1, updated.abilities.size)
        assertEquals(5, updated.abilities.find { it.name == "Athletics" }?.level)

        updated = updated.updateAbilitySpecialization("Athletics", "salto con l'asta")
        assertEquals("salto con l'asta", updated.abilities.find { it.name == "Athletics" }?.specialization)

    }

    @Test
    fun `toggleHealthBox cycles through damage types`() {
        var character = Character(attributes = Attributes(stamina = 1)) // 4 boxes
        
        // Box 0: EMPTY -> SUPERFICIAL
        character = character.toggleHealthBox(0)
        assertEquals(DamageType.SUPERFICIAL, character.health.boxes[0])
        
        // Box 0: SUPERFICIAL -> AGGRAVATED
        character = character.toggleHealthBox(0)
        assertEquals(DamageType.AGGRAVATED, character.health.boxes[0])
        
        // Box 0: AGGRAVATED -> EMPTY
        character = character.toggleHealthBox(0)
        assertEquals(DamageType.EMPTY, character.health.boxes[0])
    }


    @Test
    fun `toggleWillpowerBox cycles through damage types`() {
        var character = Character(attributes = Attributes(composure = 1, resolve = 3)) // 4 boxes

        // Box 0: EMPTY -> SUPERFICIAL
        character = character.toggleWillpowerBox(0)
        assertEquals(DamageType.SUPERFICIAL, character.willpower.boxes[0])

        // Box 0: SUPERFICIAL -> AGGRAVATED
        character = character.toggleWillpowerBox(0)
        assertEquals(DamageType.AGGRAVATED, character.willpower.boxes[0])

        // Box 0: AGGRAVATED -> EMPTY
        character = character.toggleWillpowerBox(0)
        assertEquals(DamageType.EMPTY, character.willpower.boxes[0])
    }

    @Test
    fun `addDiscipline adds new discipline with level 1`() {
        val character = Character()
        val discipline = Discipline(title = "Presence")
        val updated = character.addDiscipline(discipline)
        
        assertEquals(1, updated.disciplines.size)
        assertEquals("Presence", updated.disciplines[0].title)
        assertEquals(1, updated.disciplines[0].level)
    }

    @Test
    fun `addDisciplinePower adds new disciplinePower`() {
        val character = Character(disciplines = listOf(Discipline(title = "Presence", level = 1)))
        val disciplinePower = DisciplinePower(id = "d1", title = "Power 1", level = 1)
        var updated = character.addDisciplinePower("Presence", disciplinePower)
        assertEquals(1, updated.disciplines.size)
        assertEquals("Presence", updated.disciplines[0].title)
        assertEquals(1, updated.disciplines[0].level)
        assertEquals(1, updated.disciplines[0].selectedDisciplinePowers.size)
        assertEquals(disciplinePower, updated.disciplines[0].selectedDisciplinePowers[0])

        updated = updated.removeDisciplinePower("Presence",  disciplinePower)
        assertEquals(0, updated.disciplines[0].selectedDisciplinePowers.size)
        assertEquals(listOf<DisciplinePower>(), updated.disciplines[0].selectedDisciplinePowers)

    }


    @Test
    fun `addDiscipline adds new discipline with rituals`() {
        val character = Character()
        val ritual = Ritual(id = "r1", title = "Rituale 1", level = 1)
        val discipline = Discipline(id = "d9", title = "Stregoneria del Sangue")
        var updated = character.addDiscipline(discipline)

        updated = updated.addRitual("Stregoneria del Sangue", ritual)

        assertEquals(1, updated.disciplines.size)
        assertEquals("Stregoneria del Sangue", updated.disciplines[0].title)
        assertEquals(1, updated.disciplines[0].level)
        assertEquals(ritual, updated.learnedRituals[0])

        val updatedRitual = Ritual(id = "r1", title = "Rituale 1", level = 2, ritualsPowers = listOf())
        updated = updated.updateRitualLevel(ritual.title, 2)
        assertEquals(updatedRitual, updated.learnedRituals[0])

        val newRitualPower = RitualPower(id = "rp1", title = "Ritual Power 1", level = 1, description = "")
        updated = updated.addRitualPower(ritual.title, newRitualPower )
        assertEquals(newRitualPower, updated.learnedRituals[0].ritualsPowers[0])
        assertEquals(listOf<RitualPower>(), updated.removeRitualPower(ritual.title, newRitualPower.id).learnedRituals[0].ritualsPowers)

        val discipline2 = Discipline(id = "d10", title = "Oblio", ritual = null)
        updated = updated.addDiscipline(discipline2)
        updated = updated.removeRitual(ritual.id)
        assertEquals(2, updated.disciplines.size)
        assertEquals("Oblio", updated.disciplines[1].title)
        assertEquals(1, updated.disciplines[1].level)
        assertEquals(null, updated.disciplines[1].ritual)
        assertEquals(emptyList<Ritual>(), updated.learnedRituals)

    }



    @Test
    fun `updateDisciplineLevel updates or removes discipline`() {
        val discipline = Discipline(title = "Dominate", level = 1)
        var character = Character(disciplines = listOf(discipline))
        
        character = character.updateDisciplineLevel("Dominate", 3)
        assertEquals(3, character.disciplines[0].level)
        
        character = character.updateDisciplineLevel("Dominate", 0)
        assertTrue(character.disciplines.isEmpty())
    }

    @Test
    fun `addBackground adds background with unique identifier`() {
        val character = Character()
        val bg = Background(title = "Resources")
        val updated = character.addBackground(bg, 3)
        
        assertEquals(1, updated.backgrounds.size)
        assertEquals(3, updated.backgrounds[0].level)
        assertTrue(updated.backgrounds[0].identifier!!.isNotEmpty())
    }

    @Test
    fun `removeBackground removes by identifier`() {
        val bg = Background(title = "Resources", identifier = "id123")
        val character = Character(backgrounds = listOf(bg))
        
        val updated = character.removeBackground("id123")
        assertTrue(updated.backgrounds.isEmpty())
    }

    @Test
    fun `updateBackgroundLevel updates the level correctly`() {
        val bg = Background(title = "Resources", identifier = "id123", level = 1)
        val character = Character(backgrounds = listOf(bg))
        val updated = character.updateBackgroundLevel("id123", 3)
        assertEquals(3, updated.backgrounds[0].level)
    }

    @Test
    fun `addBackgroundMerit adds a merit to a specific background`() {
        val bg = Background(title = "Resources", identifier = "bg1")
        val character = Character(backgrounds = listOf(bg))
        val merit = Advantage(title = "Wealthy")
        val updated = character.addBackgroundMerit("bg1", merit, 2)
        
        assertEquals(1, updated.backgrounds[0].merits.size)
        assertEquals("Wealthy", updated.backgrounds[0].merits[0].title)
        assertEquals(2, updated.backgrounds[0].merits[0].level)
        assertTrue(updated.backgrounds[0].merits[0].identifier!!.isNotEmpty())
    }

    @Test
    fun `removeBackgroundMerit removes a merit from a specific background`() {
        val merit = Advantage(title = "Wealthy", identifier = "m1")
        val bg = Background(title = "Resources", identifier = "bg1", merits = listOf(merit))
        val character = Character(backgrounds = listOf(bg))
        
        val updated = character.removeBackgroundMerit("bg1", "m1")
        assertTrue(updated.backgrounds[0].merits.isEmpty())
    }

    @Test
    fun `updateBackgroundMeritLevel updates the level of a merit`() {
        val merit = Advantage(title = "Wealthy", identifier = "m1", level = 1)
        val bg = Background(title = "Resources", identifier = "bg1", merits = listOf(merit))
        val character = Character(backgrounds = listOf(bg))
        
        val updated = character.updateBackgroundMeritLevel("bg1", "m1", 3)
        assertEquals(3, updated.backgrounds[0].merits[0].level)
    }

    @Test
    fun `addBackgroundFlaw adds a flaw to a specific background`() {
        val bg = Background(title = "Resources", identifier = "bg1")
        val character = Character(backgrounds = listOf(bg))
        val flaw = Advantage(title = "Debt", isFlaw = true)
        val updated = character.addBackgroundFlaw("bg1", flaw, 1)
        
        assertEquals(1, updated.backgrounds[0].flaws.size)
        assertEquals("Debt", updated.backgrounds[0].flaws[0].title)
        assertTrue(updated.backgrounds[0].flaws[0].identifier!!.isNotEmpty())
    }

    @Test
    fun `removeBackgroundFlaw removes a flaw from a specific background`() {
        val flaw = Advantage(title = "Debt", identifier = "f1")
        val bg = Background(title = "Resources", identifier = "bg1", flaws = listOf(flaw))
        val character = Character(backgrounds = listOf(bg))
        
        val updated = character.removeBackgroundFlaw("bg1", "f1")
        assertTrue(updated.backgrounds[0].flaws.isEmpty())
    }

    @Test
    fun `updateBackgroundFlawLevel updates the level of a flaw`() {
        val flaw = Advantage(title = "Debt", identifier = "f1", level = 1)
        val bg = Background(title = "Resources", identifier = "bg1", flaws = listOf(flaw))
        val character = Character(backgrounds = listOf(bg))
        
        val updated = character.updateBackgroundFlawLevel("bg1", "f1", 2)
        assertEquals(2, updated.backgrounds[0].flaws[0].level)
    }

    @Test
    fun `addDirectFlaw adds a direct flaw to character`() {
        val character = Character()
        val flaw = Advantage(title = "Blind", isFlaw = true)
        val updated = character.addDirectFlaw(flaw, 2)
        
        assertEquals(1, updated.directFlaws.size)
        assertEquals("Blind", updated.directFlaws[0].title)
        assertTrue(updated.directFlaws[0].identifier!!.isNotEmpty())
    }

    @Test
    fun `removeDirectFlaw removes a direct flaw by identifier`() {
        val flaw = Advantage(title = "Blind", identifier = "df1")
        val character = Character(directFlaws = listOf(flaw))
        
        val updated = character.removeDirectFlaw("df1")
        assertTrue(updated.directFlaws.isEmpty())
    }

    @Test
    fun `updateDirectFlawLevel updates level of direct flaw`() {
        val flaw = Advantage(title = "Blind", identifier = "df1", level = 2)
        val character = Character(directFlaws = listOf(flaw))
        
        val updated = character.updateDirectFlawLevel("df1", 3)
        assertEquals(3, updated.directFlaws[0].level)
    }

    @Test
    fun `updateBackgroundNote updates note for background`() {
        val bg = Background(title = "Haven", identifier = "bg1")
        val character = Character(backgrounds = listOf(bg))
        val updated = character.updateBackgroundNote("bg1", "Top secret")
        assertEquals("Top secret", updated.backgrounds[0].note)
    }

    @Test
    fun `updateMeritNote updates note for merit`() {
        val merit = Advantage(title = "Beautiful", identifier = "m1")
        val bg = Background(title = "Looks", identifier = "bg1", merits = listOf(merit))
        val character = Character(backgrounds = listOf(bg))
        val updated = character.updateMeritNote("bg1", "m1", "Very pretty")
        assertEquals("Very pretty", updated.backgrounds[0].merits[0].note)
    }

    @Test
    fun `updateFlawNote updates note for flaw`() {
        val flaw = Advantage(title = "Ugly", identifier = "f1")
        val bg = Background(title = "Looks", identifier = "bg1", flaws = listOf(flaw))
        val character = Character(backgrounds = listOf(bg))
        val updated = character.updateFlawNote("bg1", "f1", "Not so pretty")
        assertEquals("Not so pretty", updated.backgrounds[0].flaws[0].note)
    }

    @Test
    fun `updateDirectFlawNote updates note for direct flaw`() {
        val flaw = Advantage(title = "Blind", identifier = "df1")
        val character = Character(directFlaws = listOf(flaw))
        val updated = character.updateDirectFlawNote("df1", "Needs help")
        assertEquals("Needs help", updated.directFlaws[0].note)
    }

    @Test
    fun `updateAdvantage updates merit or flaw level inside background based on title match`() {
        val merit = Advantage(id = 1, title = "M1", level = 1, isFlaw = false)
        val flaw = Advantage(id = 2, title = "F1", level = 1, isFlaw = true)
        val bg = Background(title = "Resources", merits = listOf(merit), flaws = listOf(flaw))
        val character = Character(backgrounds = listOf(bg))
        
        // Update merit
        var updated = character.updateAdvantage(merit, bg, 3)
        assertEquals(3, updated.backgrounds[0].merits[0].level)
        
        // Update flaw
        updated = character.updateAdvantage(flaw, bg, 2)
        assertEquals(2, updated.backgrounds[0].flaws[0].level)
    }

    @Test
    fun `addAdvantageFlaw adds a flaw specifically to background flaws list`() {
        val bg = Background(title = "Haven")
        val character = Character(backgrounds = listOf(bg))
        val flaw = Advantage(title = "Creepy")
        val updated = character.addAdvantageFlaw(flaw, bg, 1)
        
        assertEquals(1, updated.backgrounds[0].flaws.size)
        assertEquals("Creepy", updated.backgrounds[0].flaws[0].title)
    }

    @Test
    fun `removeAdvantageFlaw removes a merit from background merits list`() {
        // NOTE: The implementation actually removes from MERITS list
        val merit = Advantage(title = "Nice")
        val bg = Background(title = "Haven", merits = listOf(merit))
        val character = Character(backgrounds = listOf(bg))
        val updated = character.removeAdvantageFlaw(merit, bg)
        
        assertTrue(updated.backgrounds[0].merits.isEmpty())
    }

    @Test
    fun `updateAdvantageFlawLevel updates merit level in background merits list`() {
        // NOTE: The implementation updates MERITS list
        val merit = Advantage(id = 1, title = "Nice", level = 1)
        val bg = Background(title = "Haven", merits = listOf(merit))
        val character = Character(backgrounds = listOf(bg))
        val updated = character.updateAdvantageFlawLevel(merit, bg, 5)
        
        assertEquals(5, updated.backgrounds[0].merits[0].level)
    }

    @Test
    fun `removeAdvantage removes merit or flaw from background based on isFlaw flag`() {
        val merit = Advantage(title = "M1", isFlaw = false)
        val flaw = Advantage(title = "F1", isFlaw = true)
        val bg = Background(title = "BG", merits = listOf(merit), flaws = listOf(flaw))
        val character = Character(backgrounds = listOf(bg))
        
        var updated = character.removeAdvantage(merit, bg)
        assertTrue(updated.backgrounds[0].merits.isEmpty())
        assertEquals(1, updated.backgrounds[0].flaws.size)
        
        updated = character.removeAdvantage(flaw, bg)
        assertTrue(updated.backgrounds[0].flaws.isEmpty())
    }

    @Test
    fun `addAdvantage adds merit or flaw to background based on isFlaw flag`() {
        val merit = Advantage(title = "M1", isFlaw = false)
        val flaw = Advantage(title = "F1", isFlaw = true)
        val bg = Background(title = "BG")
        val character = Character(backgrounds = listOf(bg))
        
        var updated = character.addAdvantage(merit, bg, 2)
        assertEquals(1, updated.backgrounds[0].merits.size)
        assertEquals(2, updated.backgrounds[0].merits[0].level)
        
        updated = updated.addAdvantage(flaw, bg, 1)
        assertEquals(1, updated.backgrounds[0].flaws.size)
        assertEquals(1, updated.backgrounds[0].flaws[0].level)
    }

    @Test
    fun `addLoresheet adds loresheet if not already present`() {
        val character = Character()
        val lore = Loresheet(title = "First Edition")
        var updated = character.addLoresheet(lore, 3)
        
        assertEquals(1, updated.loresheets.size)
        assertEquals("First Edition", updated.loresheets[0].title)
        assertEquals(3, updated.loresheets[0].level)
        
        // Add again - should not add duplicate
        updated = updated.addLoresheet(lore, 5)
        assertEquals(1, updated.loresheets.size)
    }

    @Test
    fun `removeLoresheet removes by title`() {
        val lore = Loresheet(title = "First Edition")
        val character = Character(loresheets = listOf(lore))
        val updated = character.removeLoresheet("First Edition")
        assertTrue(updated.loresheets.isEmpty())
    }

    @Test
    fun `updateLoresheetLevel updates level by title`() {
        val lore = Loresheet(title = "First Edition", level = 1)
        val character = Character(loresheets = listOf(lore))
        val updated = character.updateLoresheetLevel("First Edition", 4)
        assertEquals(4, updated.loresheets[0].level)
    }
}
