package com.example.gestiontienda2.data.local.room.entities.mapper

import com.example.gestiontienda2.data.remote.firebase.models.ClientFirebase
import com.example.gestiontienda2.domain.models.Client

// Conversiones entre modelo dominio <-> modelo firebase

fun ClientFirebase.toDomain(): Client = Client(
    id = this.id.toIntOrNull() ?: 0,
    name = this.name,
    phone = this.phone.toString(),
    address = "",          // Firebase no tiene address, ajusta si agregas
    email = this.email,
    paymentPreference = "efectivo" // Valor por defecto o ajusta según tu lógica
)

fun Client.toFirebase(): ClientFirebase = ClientFirebase(
    id = this.id.toString(),
    name = this.name,
    phone = this.phone ?: "",
    email = this.email ?: "",
    id2 = TODO(),
    id1 = TODO()
)
