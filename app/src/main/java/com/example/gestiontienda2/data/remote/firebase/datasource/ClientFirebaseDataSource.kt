package com.example.gestiontienda2.data.remote.firebase.datasource

import com.example.gestiontienda2.data.remote.firebase.models.ClientFirebase
import kotlinx.coroutines.flow.Flow

interface ClientFirebaseDataSource {
    fun getAllClients(): Flow<List<ClientFirebase>>
    suspend fun getClientById(clientId: String): ClientFirebase?
    suspend fun addClient(client: ClientFirebase)
    suspend fun updateClient(client: ClientFirebase)
    suspend fun deleteClient(clientId: String) // Often by ID
}
