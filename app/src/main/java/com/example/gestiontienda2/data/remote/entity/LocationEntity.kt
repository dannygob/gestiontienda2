package com.example.gestiontienda2.data.remote.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class Location(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val aisle: String,
    val rack: String,
    val level: String,
)