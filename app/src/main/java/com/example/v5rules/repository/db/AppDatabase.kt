package com.example.v5rules.repository.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.v5rules.data.converter.*
import com.example.v5rules.repository.dao.CharacterDao
import com.example.v5rules.data.Character
import com.example.v5rules.data.FavoriteNpc
import com.example.v5rules.repository.dao.FavoriteNpcDao

@Database(
    entities = [Character::class, FavoriteNpc::class],
    version = 4,
    autoMigrations = [
        AutoMigration (
            from = 1,
            to = 2,
            spec = Migration1To2::class
        ),
        AutoMigration (
            from = 2,
            to = 3
        ),
        AutoMigration (
            from = 3,
            to = 4
        ),
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
    abstract fun favoriteNpcDao(): FavoriteNpcDao

    companion object {
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE `favorite_npcs` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`name` TEXT NOT NULL, " +
                            "`secondName` TEXT, " +
                            "`familyName` TEXT NOT NULL, " +
                            "`nationality` TEXT NOT NULL)"
                )
            }
        }
    }
}

