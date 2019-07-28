package com.rajeshjadav.android.mvputilityapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.rajeshjadav.android.mvputilityapp.ui.contacts.Contact

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveContact(contact: Contact)

    @Delete
    fun deleteContact(contact: Contact)
}
