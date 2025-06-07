package com.example.gestiontienda2.data.remote.firebase.datasource.impl

import com.example.gestiontienda2.data.local.room.dao.ClientDao
import com.example.gestiontienda2.data.local.room.entities.mapper.toDomain
import com.example.gestiontienda2.data.local.room.entities.mapper.toEntity
import com.example.gestiontienda2.data.local.room.entities.mapper.toFirebase
import com.example.gestiontienda2.data.remote.firebase.datasource.source.ClientFirebaseDataSource

import com.example.gestiontienda2.domain.models.Client
import com.example.gestiontienda2.domain.repository.ClientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ClientRepositoryImpl @Inject constructor(
    private val clientDao: ClientDao,
    private val clientFirebaseDataSource: ClientFirebaseDataSource,
) : ClientRepository {

    override fun getAllClients(): Flow<List<Client>> = flow {
        try {
            val firebaseClients = clientFirebaseDataSource.getClients()
            withContext(Dispatchers.IO) {
                clientDao.insertAllClients(firebaseClients.map { it.toEntity() })
            }
        } catch (e: Exception) {
            // Manejo de error opcional
        }
        emitAll(
            clientDao.getAllClients().map { list -> list.map { it.toDomain() } }
        )
    }

    override suspend fun getClientById(id: Int): Client? {
        return try {
            clientFirebaseDataSource.getClientById(id.toString())?.toDomain()
                ?: clientDao.getClientById(id)?.toDomain()
        } catch (e: Exception) {
            clientDao.getClientById(id)?.toDomain()
        }
    }

    override suspend fun insertClient(client: Client) {
        withContext(Dispatchers.IO) {
            clientDao.insertClient(client.toEntity())
        }
        try {
            clientFirebaseDataSource.addClient(client.toFirebase())
        } catch (e: Exception) {
            // Manejo de error opcional
        }
    }

    override suspend fun updateClient(client: Client) {
        withContext(Dispatchers.IO) {
            clientDao.updateClient(client.toEntity())
        }
        try {
            clientFirebaseDataSource.updateClient(client.toFirebase())
        } catch (e: Exception) {
            // Manejo de error opcional
        }
    }

    override suspend fun deleteClient(client: Client) {
        withContext(Dispatchers.IO) {
            clientDao.deleteClient(client.toEntity())
        }
        try {
            clientFirebaseDataSource.deleteClient(client.id.toString())
        } catch (e: Exception) {
            // Manejo de error opcional
        }
    }
}
