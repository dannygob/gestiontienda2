package com.example.gestiontienda2.data.local.room.entities.mapper

import com.example.gestiontienda2.data.local.room.entities.entity.ClientEntity
import com.example.gestiontienda2.data.remote.firebase.models.ClientFirebase
import com.example.gestiontienda2.domain.models.Client
import kotlin.text.toIntOrNull

fun ClientFirebase.toEntity() = ClientEntity(
    id = id.toIntOrNull() ?: 0,
    name = name,
    email = email.toString(),
    address = "", // Assuming address is not available in Firebase model
    paymentPreference = " efectivo", // Assuming a default value for payment preference
    phone = phone,

    )

fun ClientFirebase.toDomain() = Client(
    id = id.toIntOrNull() ?: 0,
    name = name,
    email = email,
    address = "", // Assuming address is not available in Firebase model
    paymentPreference = " efectivo", // Assuming a default value for payment preference
    phone = phone,
)

fun ClientEntity.toDomain() = Client(
    id = id.toIntOrNull() ?: 0,
    name = name,
    email = email,
    address = "", // Assuming address is not available in Firebase model
    paymentPreference = " efectivo", // Assuming a default value for payment preference
    phone = phone,
)

fun Client.toEntity() = ClientEntity(
    id = id.toIntOrNull() ?: 0,
    name = name,
    email = email,
    address = "", // Assuming address is not available in Firebase model
    paymentPreference = " efectivo", // Assuming a default value for payment preference
    phone = phone,
)

fun Client.toFirebase() = ClientFirebase(
    id = (id.toIntOrNull() ?: 0),
    name = name,
    email = email,
    address = "", // Assuming address is not available in Firebase model
    paymentPreference = " efectivo", // Assuming a default value for payment preference
    phone = phone,
)
