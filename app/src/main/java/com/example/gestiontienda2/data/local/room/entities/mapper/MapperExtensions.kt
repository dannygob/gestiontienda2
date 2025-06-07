package com.example.gestiontienda2.data.local.room.entities.mapper

import com.example.gestiontienda2.data.local.room.entities.entity.ClientEntity
import com.example.gestiontienda2.data.remote.firebase.models.ClientFirebase
import com.example.gestiontienda2.domain.models.Client


fun ClientFirebase.toEntity(): ClientEntity {
    return ClientEntity(
        id = id.toIntOrNull() ?: 0,
        name = name,
        email = email.toString(),
        phone = phone
    )
}

fun ClientEntity.toDomain(): Client {
    return Client(
        id = id,
        name = name,
        email = email,
        phone = phone
    )
}

fun Client.toEntity(): ClientEntity {
    return ClientEntity(
        id = id,
        name = name,
        email = email.toString(),
        phone = phone
    )
}

fun Client.toFirebase(): ClientFirebase {
    return ClientFirebase(
        id2 = id.toIntOrNull() ?: 0,
        id1 = id.toIntOrNull() ?: 0,
        id = id.toString(),
        name = name,
        email = email,
        phone = phone
    )
}