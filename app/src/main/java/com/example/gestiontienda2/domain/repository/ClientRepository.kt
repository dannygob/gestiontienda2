package com.example.gestiontienda2.domain.repository

import com.example.gestiontienda2.domain.models.Client
import kotlinx.coroutines.flow.Flow

interface ClientRepository {

    suspend fun insertClient(client: Client)
    fun getAllClients(): Flow<List<Client>>
    suspend fun getClientById(id: Int): Client?
    suspend fun updateClient(client: Client)
    suspend fun deleteClient(client: Client)
    suspend fun addClient(client: Client)
    suspend fun getClients(): Flow<List<Client>>
}