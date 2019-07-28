package com.rajeshjadav.android.mvputilityapp.data.database.converter

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class StringListConverter {

    @TypeConverter
    fun toJson(list: List<String>?): String {
        return Moshi.Builder().build().adapter<List<String>?>(List::class.java).toJson(list)
    }

    @TypeConverter
    fun fromJson(value: String?): List<String> {
        val listType = Types.newParameterizedType(List::class.java, String::class.java)
        val adapter: JsonAdapter<List<String>> = Moshi.Builder().build().adapter(listType)
        return adapter.fromJson(value ?: "[]") ?: emptyList()
    }
}
