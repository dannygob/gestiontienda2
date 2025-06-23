package com.example.gestiontienda2.data.mapper

import androidx.room.TypeConverter
import java.util.Date

class MyConverters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}


