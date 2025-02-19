package com.example.v5rules.repository.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.v5rules.data.converter.*
import com.example.v5rules.repository.dao.CharacterDao
import com.example.v5rules.data.Character

@Database(
    entities = [Character::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration (from = 1, to = 2)
    ]
)
@TypeConverters(
    ClanConverter::class,
    AttributesConverter::class,
    AbilitiesConverter::class,
    DisciplineListConverter::class,
    AdvantageListConverter::class,
    BackgroundListConverter::class,
    LoresheetListConverter::class,
    HealthConverter::class,
    WillpowerConverter::class,
    HumanityConverter::class,
    ExperienceConverter::class
)

// Metti le entità
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}