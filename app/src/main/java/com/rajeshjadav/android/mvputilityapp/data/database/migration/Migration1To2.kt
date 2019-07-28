package com.rajeshjadav.android.mvputilityapp.data.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration1To2 : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add column query
        database.execSQL("ALTER TABLE Contacts ADD COLUMN address TEXT")

        // Change the temp table name to old
        database.execSQL("ALTER TABLE RecentSearches_New RENAME TO RecentSearches")
    }
}