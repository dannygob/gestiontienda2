package com.example.gestiontienda2.data.repository

import com.gestiontienda2.data.local.entities.toEntity
import com.gestiontienda2.data.local.room.dao.ClientDao
import com.gestiontienda2.data.remote.firebase.ClientFirebaseDataSource
import com.gestiontienda2.data.remote.firebase.toDomain
import com.gestiontienda2.data.remote.firebase.toFirebase
import com.gestiontienda2.domain.models.Client
import com.gestiontienda2.domain.repository.ClientRepository
import com.google.android.gms.common.api.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ClientFirebaseDataSource {

}

class ClientRepositoryImpl @Inject constructor(
    private val clientDao: ClientDao,
    private val clientFirebaseDataSource: ClientFirebaseDataSource
) : ClientRepository {

    override fun getAllClients(): Flow<List<Api.Client>> {
        return flow {
            // Try to fetch from Firebase first
            try {
                // Assuming clientFirebaseDataSource.getClients() returns a List<ClientFirebase>
                val firebaseClients = clientFirebaseDataSource.getClients()
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
                val firebaseClient =
                    clientFirebaseDataSource.getClientById(id.toString()) // Assuming Firebase uses String ID
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