package com.example.v5rules.repository.db

import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec

@RenameColumn.Entries(
    RenameColumn(
        tableName = "characters",
        fromColumnName = "directFlaws",
        toColumnName = "flawSummaries" // Replace with your NEW column name
    )
)
class Migration1To2 : AutoMigrationSpec