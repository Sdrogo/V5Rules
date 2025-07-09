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
    version = 3,
    autoMigrations = [
        AutoMigration (
            from = 1,
            to = 2,
            spec = Migration1To2::class
        ),
        AutoMigration (
            from = 2,
            to = 3
        )
    ],
    exportSchema = true
)
@TypeConverters(
    ClanConverter::class,
    AttributesConverter::class,
    AbilityListConverter::class,
    DisciplineListConverter::class,
    BackgroundListConverter::class,
    LoresheetListConverter::class,
    HealthConverter::class,
    WillpowerConverter::class,
    HumanityConverter::class,
    ExperienceConverter::class,
    PredatorConverter::class,
    AdvantageListConverter::class
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}