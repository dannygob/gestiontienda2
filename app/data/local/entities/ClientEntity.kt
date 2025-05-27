package com.your_app_name.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.your_app_name.domain.models.Client

@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val phone: String,
    val address: String,
    val email: String
)

fun ClientEntity.toDomainModel(): Client {
    return Client(
        id = this.id,
        name = this.name,
        phone = this.phone,
        address = this.address,
        email = this.email
    )
}

fun Client.toEntity(): ClientEntity {
    return ClientEntity(
        id = this.id, // Assuming your domain Client has an ID property
        name = this.name,
        phone = this.phone,
        address = this.address,
        email = this.email
    )
}