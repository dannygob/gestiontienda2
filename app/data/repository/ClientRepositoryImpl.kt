package com.your_app_name.data.repository

import com.your_app_name.data.local.dao.ClientDao
import com.your_app_name.data.local.entities.toEntity
import com.your_app_name.data.remote.firebase.ClientFirebaseDataSource
import com.your_app_name.data.remote.firebase.toDomain
import com.your_app_name.data.remote.firebase.toFirebase
import com.your_app_name.domain.models.Client
import com.your_app_name.domain.repository.ClientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ClientRepositoryImpl @Inject constructor(
    private val clientDao: ClientDao,
    private val clientFirebaseDataSource: ClientFirebaseDataSource
    ) : ClientRepository {

    override fun getAllClients(): Flow<List<Client>> {
        return flow {
            // Try to fetch from Firebase first
            try {
                // Assuming clientFirebaseDataSource.getAllClients() returns a List<ClientFirebase>
                val firebaseClients = clientFirebaseDataSource.getAllClients()
                withContext(Dispatchers.IO) {
                    clientDao.insertAllClients(firebaseClients.map { it.toEntity() })
                }
            } catch (e: Exception) {
                // Handle Firebase fetch errors (e.g., offline)
            }
        emitAll(clientDao.getAllClients().map { entities ->
            entities.map { it.toDomain() }
        })
    }

    override suspend fun getClientById(id: Int): Client? {
        // Prioritize Firebase if online, otherwise get from Room
        return try {
            // Assuming clientFirebaseDataSource.getClientById() returns ClientFirebase?
            val firebaseClient = clientFirebaseDataSource.getClientById(id.toString()) // Assuming Firebase uses String ID
            firebaseClient?.toDomain()
        } catch (e: Exception) {
            // Handle Firebase fetch errors (e.g., offline)
            clientDao.getClientById(id)?.toDomain()
        }
    }

    override suspend fun insertClient(client: Client) {
        // Example: Insert into Room and Firebase
        withContext(Dispatchers.IO) {
            clientDao.insertClient(client.toEntity())
        }
        try {
            clientFirebaseDataSource.addClient(client.toFirebase())
        } catch (e: Exception) {
            // Handle Firebase insertion errors (e.g., offline)
            // You might want a mechanism to sync this later
        }
    }

    override suspend fun updateClient(client: Client) {
        withContext(Dispatchers.IO) {
            clientDao.updateClient(client.toEntity())
        }
        try {
            // Assuming clientFirebaseDataSource.updateClient() uses ClientFirebase object
            clientFirebaseDataSource.updateClient(client.toFirebase())
        } catch (e: Exception) {
            // Handle Firebase update errors (e.g., offline)
            // You might want a mechanism to sync this later
        }
    }

    override suspend fun deleteClient(client: Client) {
        clientDao.deleteClient(client.toEntity())
        clientFirebaseDataSource.deleteClient(client.id.toString()) // Assuming Firebase delete uses ID string
    }
}