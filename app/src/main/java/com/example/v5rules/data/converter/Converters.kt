package com.example.v5rules.data.converter

import androidx.room.TypeConverter
import com.example.v5rules.data.Ability
import com.example.v5rules.data.Attributes
import com.example.v5rules.data.Background
import com.example.v5rules.data.Clan
import com.example.v5rules.data.Discipline
import com.example.v5rules.data.Experience
import com.example.v5rules.data.Health
import com.example.v5rules.data.Humanity
import com.example.v5rules.data.Loresheet
import com.example.v5rules.data.PredatorType
import com.example.v5rules.data.Willpower
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ClanConverter {
    @TypeConverter
    fun fromClan(clan: Clan?): String? {
        return Gson().toJson(clan)
    }

    @TypeConverter
    fun toClan(clanString: String?): Clan? {
        return Gson().fromJson(clanString, Clan::class.java)
    }
}

class PredatorConverter {
    @TypeConverter
    fun fromPredator(predator: PredatorType?): String? {
        return Gson().toJson(predator)
    }

    @TypeConverter
    fun toPredator(predatorString: String?): PredatorType? {
        return Gson().fromJson(predatorString, PredatorType::class.java)
    }
}
class AttributesConverter {
    @TypeConverter
    fun fromAttributes(attributes: Attributes): String {
        return Gson().toJson(attributes)
    }

    @TypeConverter
    fun toAttributes(attributesString: String): Attributes {
        return Gson().fromJson(attributesString, Attributes::class.java)
    }
}

// Abilities
class AbilityListConverter {
    private val gson = Gson() // Inizializza Gson una sola volta

    @TypeConverter
    fun fromAbilityList(abilities: List<Ability>?): String? {
        if (abilities == null) {
            return null
        }
        return gson.toJson(abilities)
    }

    @TypeConverter
    fun toAbilityList(abilitiesString: String?): List<Ability>? {
        if (abilitiesString == null) {
            return null
        }
        val listType = object : TypeToken<List<Ability>>() {}.type
        return gson.fromJson(abilitiesString, listType)
    }
}


// List<Discipline>
class DisciplineListConverter {
    @TypeConverter
    fun fromDisciplineList(disciplines: List<Discipline>): String {
        return Gson().toJson(disciplines)
    }

    @TypeConverter
    fun toDisciplineList(disciplinesString: String): List<Discipline> {
        val listType = object: TypeToken<List<Discipline>>() {}.type
        return Gson().fromJson(disciplinesString, listType)
    }
}


// List<Background>
class BackgroundListConverter {
    @TypeConverter
    fun fromBackgroundList(backgrounds: List<Background>): String {
        return Gson().toJson(backgrounds)
    }

    @TypeConverter
    fun toBackgroundList(backgroundsString: String): List<Background> {
        val listType = object: TypeToken<List<Background>>() {}.type
        return Gson().fromJson(backgroundsString, listType)
    }
}

// List<Loresheet>
class LoresheetListConverter {
    @TypeConverter
    fun fromLoresheetList(loresheets: List<Loresheet>): String {
        return Gson().toJson(loresheets)
    }

    @TypeConverter
    fun toLoresheetList(loresheetsString: String): List<Loresheet> {
        val listType = object: TypeToken<List<Loresheet>>() {}.type
        return Gson().fromJson(loresheetsString, listType)
    }
}

// Health
class HealthConverter {
    @TypeConverter
    fun fromHealth(health: Health): String {
        return Gson().toJson(health)
    }

    @TypeConverter
    fun toHealth(healthString: String): Health {
        return Gson().fromJson(healthString, Health::class.java)
    }
}

// Willpower
class WillpowerConverter {
    @TypeConverter
    fun fromWillpower(willpower: Willpower): String {
        return Gson().toJson(willpower)
    }

    @TypeConverter
    fun toWillpower(willpowerString: String): Willpower {
        return Gson().fromJson(willpowerString, Willpower::class.java)
    }
}

// Humanity
class HumanityConverter {
    @TypeConverter
    fun fromHumanity(humanity: Humanity): String {
        return Gson().toJson(humanity)
    }

    @TypeConverter
    fun toHumanity(humanityString: String): Humanity {
        return Gson().fromJson(humanityString, Humanity::class.java)
    }
}

// Experience
class ExperienceConverter {
    @TypeConverter
    fun fromExperience(experience: Experience): String {
        return Gson().toJson(experience)
    }

    @TypeConverter
    fun toExperience(experienceString: String): Experience {
        return Gson().fromJson(experienceString, Experience::class.java)
    }
}
