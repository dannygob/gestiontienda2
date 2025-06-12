package com.example.gestiontienda2.data.local.room.entities.mapper

import com.example.gestiontienda2.data.local.room.entities.entity.ClientEntity
import com.example.gestiontienda2.data.remote.firebase.models.ClientFirebase
import com.example.gestiontienda2.domain.models.Client
import kotlin.text.toIntOrNull

fun ClientFirebase.toEntity() = ClientEntity(
    id = id.toIntOrNull() ?: 0, // Assuming id from Firebase can be String, converting to Int for Entity
    name = name,
    email = email ?: "", // Handle null email from Firebase
    address = address ?: "", // Map Firebase address, default to empty if null
    paymentPreference = paymentPreference ?: "", // Map Firebase preference, default to empty if null
    phone = phone ?: ""
)

fun ClientFirebase.toDomain() = Client(
    id = id.toIntOrNull() ?: 0, // Assuming id from Firebase can be String, converting to Int for Domain
    name = name,
    email = email, // Domain Client email is nullable
    address = address ?: "", // Map Firebase address, default to empty if null
    paymentPreference = paymentPreference ?: "", // Map Firebase preference, default to empty if null
    phone = phone ?: ""
)

fun ClientEntity.toDomain() = Client(
    id = id, // Direct assignment
    name = name,
    email = email, // Entity email is non-null, Domain Client email is nullable
    address = address, // Map from entity
    paymentPreference = paymentPreference, // Map from entity
    phone = phone
)

fun Client.toEntity() = ClientEntity(
    id = id, // Direct assignment
    name = name,
    email = email ?: "", // Handle nullable domain email for non-nullable entity email
    address = address, // Map from domain
    paymentPreference = paymentPreference, // Map from domain
    phone = phone
)

fun Client.toFirebase() = ClientFirebase(
    id = id.toString(), // Convert Int ID to String for Firebase
    name = name,
    email = email, // Firebase Client email is nullable
    address = address, // Map from domain
    paymentPreference = paymentPreference, // Map from domain
    phone = phone
)
