package com.your_app_name.domain.repository

import com.your_app_name.domain.models.Client
import kotlinx.coroutines.flow.Flow

interface ClientRepository {

    suspend fun insertClient(client: Client)

    fun getAllClients(): Flow<List<Client>>

    suspend fun getClientById(clientId: Int): Client?

    suspend fun addClient(client: Client)

    suspend fun updateClient(client: Client)

    suspend fun deleteClient(client: Client)
}