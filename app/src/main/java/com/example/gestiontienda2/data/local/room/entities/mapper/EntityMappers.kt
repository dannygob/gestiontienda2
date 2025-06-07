package com.example.gestiontienda2.data.local.room.entities.mapper

import com.example.gestiontienda2.data.local.room.entities.entity.ClientEntity
import com.example.gestiontienda2.domain.models.Client

// De Entity (base de datos) a Domain (lógica)
fun ClientEntity.toDomain(): Client = Client(
    id = this.id.toInt(),
    name = this.name,
    phone = this.phone,
    address = this.address,
    email = this.email
)

// De Domain a Entity
fun Client.toEntity(): ClientEntity = ClientEntity(
    id = this.id.toLong(),
    name = this.name,
    phone = this.phone,
    address = this.address,
    email = this.email
        ?: "", // En el entity no es nullable, si en domain es null, le asignamos cadena vacía
    paymentPreference = "" // Aquí no tienes ese campo en domain, así que debe ir con algún valor por defecto, o agregarlo al modelo
)
