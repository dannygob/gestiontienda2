package com.example.gestiontienda2.data.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gestiontienda2.domain.models.Client

@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val phone: String? = null,
    val email: String? = null,
    val address: String? = null
)

fun ClientEntity.toDomain(): Client {
    return Client(
        id = this.id.toString(), // Adjust if domain Client id is Int
        name = this.name,
        phone = this.phone ?: "",
        email = this.email ?: "",
        address = this.address ?: ""
    )
}

fun Client.toEntity(): ClientEntity {
    return ClientEntity(
        id = this.id.toIntOrNull() ?: 0,
        name = this.name,
        phone = this.phone,
        email = this.email,
        address = this.address
    )
}
