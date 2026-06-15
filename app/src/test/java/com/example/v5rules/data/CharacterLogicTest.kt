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
        val updated = character.updateAbilityLevel("Athletics", 3)
        assertEquals(3, updated.abilities.find { it.name == "Athletics" }?.level)

        val updated2 = updated.updateAbilityLevel("Athletics", 5)
        assertEquals(1, updated2.abilities.size)
        assertEquals(5, updated2.abilities.find { it.name == "Athletics" }?.level)
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
    fun `addDiscipline adds new discipline with rituals`() {
        val character = Character()
        val ritual = Ritual(id = "r1", title = "Rituale 1")
        val discipline = Discipline(id = "d9", title = "Stregoneria del Sangue", ritual = ritual)
        var updated = character.addDiscipline(discipline)

        assertEquals(1, updated.disciplines.size)
        assertEquals("Stregoneria del Sangue", updated.disciplines[0].title)
        assertEquals(1, updated.disciplines[0].level)
        assertEquals(ritual, updated.disciplines[0].ritual)

        val discipline2 = Discipline(id = "d10", title = "Oblio", ritual = null)
        updated = updated.addDiscipline(discipline2)

        assertEquals(2, updated.disciplines.size)
        assertEquals("Oblio", updated.disciplines[1].title)
        assertEquals(1, updated.disciplines[1].level)
        assertEquals(null, updated.disciplines[1].ritual)



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
    fun `updateHunger coerces value between 0 and 5`() {
        val character = Character()
        assertEquals(0, character.updateHunger(-1).hunger)
        assertEquals(5, character.updateHunger(10).hunger)
        assertEquals(3, character.updateHunger(3).hunger)
    }
}
