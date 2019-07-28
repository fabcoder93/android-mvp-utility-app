package com.rajeshjadav.android.mvputilityapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rajeshjadav.android.mvputilityapp.R
import com.rajeshjadav.android.mvputilityapp.data.database.converter.StringListConverter
import com.rajeshjadav.android.mvputilityapp.data.database.dao.ContactDao
import com.rajeshjadav.android.mvputilityapp.data.database.migration.Migration1To2
import com.rajeshjadav.android.mvputilityapp.ui.contacts.Contact
import com.rajeshjadav.android.mvputilityapp.util.SingletonHolder


@Database(
    entities = [Contact::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    value = [
        StringListConverter::class
    ]
)
abstract class LocalDatabase : RoomDatabase() {

    companion object : SingletonHolder<LocalDatabase, Context>(
        {
            Room.databaseBuilder(
                it.applicationContext,
                LocalDatabase::class.java,
                it.getString(R.string.database_name)
            ).addMigrations(Migration1To2()).build()
        })

    abstract fun contactDao(): ContactDao


}
