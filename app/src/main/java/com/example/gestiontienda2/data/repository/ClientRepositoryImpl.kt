package com.example.gestiontienda2.data.repository


import com.example.gestiontienda2.data.local.dao.ClientDao
import com.example.gestiontienda2.data.mapper.toEntity
import com.example.gestiontienda2.data.mapper.toFirebase
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
    private val clientFirebaseDataSource: ClientFirebaseDataSource
) : ClientRepository {

    override fun getAllClients(): Flow<List<Client>> {
        return flow {
            try {
                val firebaseClients = clientFirebaseDataSource.getClients()
                withContext(Dispatchers.IO) {
                    clientDao.insertAllClients(firebaseClients.map { it.toEntity() })
                }
            } catch (e: Exception) {
                // Manejo de errores de Firebase
            }
            emitAll(clientDao.getAllClients().map { entities ->
                entities.map { it.toDomain(productsMap) }
            })
        }
    }

    override suspend fun getClientById(id: Int): Client? {
        return try {
            val firebaseClient = clientFirebaseDataSource.getClientById(id.toString())
            firebaseClient?.toDomain(productsMap)
        } catch (e: Exception) {
            clientDao.getClientById(id)?.toDomain(productsMap)
        } as Client?
    }

    override suspend fun insertClient(client: Client) {
        withContext(Dispatchers.IO) {
            clientDao.insertClient(client.toEntity())
        }
        try {
            clientFirebaseDataSource.addClient(client.toFirebase())
        } catch (e: Exception) {
            // Manejo de error al agregar cliente
        }
    }

    override suspend fun updateClient(client: Client) {
        withContext(Dispatchers.IO) {
            clientDao.updateClient(client.toEntity())
        }
        try {
            clientFirebaseDataSource.updateClient(client.toFirebase())
        } catch (e: Exception) {
            // Manejo de error al actualizar cliente
        }
    }

    override suspend fun deleteClient(client: Client) {
        withContext(Dispatchers.IO) {
            clientDao.deleteClient(client.toEntity())
        }
        try {
            clientFirebaseDataSource.deleteClient(client.id.toString())
        } catch (e: Exception) {
            // Manejo de error al eliminar cliente
        }
    }
}
