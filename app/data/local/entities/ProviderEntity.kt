package com.your_app_name.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.your_app_name.domain.models.Provider

@Entity(tableName = "providers")
data class ProviderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val phone: String,
    val address: String,
    val email: String
)

fun ProviderEntity.toDomainModel(): Provider {
    return Provider(
        id = id,
        name = name,
        phone = phone,
        address = address,
        email = email
    )
}

fun Provider.toEntity(): ProviderEntity {
    return ProviderEntity(
        id = id,
        name = name,