package com.example.gestiontienda2.data.local.room.converters

import androidx.room.TypeConverter

class MyConverters {
    @TypeConverter
    fun fromList(value: List<String>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toList(value: String): List<String> {
        return value.split(",")
    }
}

