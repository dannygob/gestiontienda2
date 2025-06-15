package com.example.gestiontienda2.data.repository

import com.example.gestiontienda2.data.local.room.dao.ClientDao
import com.example.gestiontienda2.data.remote.firebase.datasource.ClientFirebaseDataSource // Ensure this interface exists
import com.example.gestiontienda2.domain.models.Client
import com.example.gestiontienda2.domain.repository.ClientRepository
import com.example.gestiontienda2.data.mappers.toDomain
import com.example.gestiontienda2.data.mappers.toEntity
import com.example.gestiontienda2.data.mappers.toFirebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

// Note: Assuming ClientFirebaseDataSourceImpl will be provided by Hilt for ClientFirebaseDataSource interface
@Singleton // Typically repositories are singletons
class ClientRepositoryImpl @Inject constructor(
    private val clientDao: ClientDao,
    private val clientFirebaseDataSource: ClientFirebaseDataSource 
) : ClientRepository {

    override fun getAllClients(): Flow<List<Client>> {
        return flow {
            try {
                // Attempt to fetch from Firebase and update Room
                // This is a simplified sync; real-world sync can be more complex (e.g., handling conflicts, timestamping)
                // Assuming clientFirebaseDataSource.getAllClients() returns Flow<List<ClientFirebase>>
                clientFirebaseDataSource.getAllClients().collect { firebaseClients ->
                    val clientEntities = firebaseClients.map { it.toDomain().toEntity() } // Firebase -> Domain -> Entity
                    // Consider more sophisticated insert/update logic if needed (e.g. insertAll an onConflictStrategy in DAO)
                    clientDao.insertAllClients(clientEntities) 
                }
            } catch (e: Exception) {
                // Log.e("ClientRepo", "Error fetching from Firebase, proceeding with local data", e)
                // Optionally emit an error state or specific signal to UI if Firebase sync fails
            }
            // Always emit from Room as the primary source of truth for the UI
            emitAll(clientDao.getAllClients().map { entities ->
                entities.map { it.toDomain() }
            })
        }.flowOn(Dispatchers.IO) // Perform operations on IO dispatcher
    }

    override suspend fun getClientById(id: Int): Client? {
        return withContext(Dispatchers.IO) {
            try {
                val firebaseClient = clientFirebaseDataSource.getClientById(id.toString()) // Firebase ID is String
                if (firebaseClient != null) {
                    val domainClient = firebaseClient.toDomain()
                    // Cache in Room
                    clientDao.insertClient(domainClient.toEntity()) // insertClient handles conflict with OnConflictStrategy.REPLACE
                    return@withContext domainClient
                }
            } catch (e: Exception) {
                // Log.e("ClientRepo", "Error fetching client $id from Firebase", e)
            }
            // Fetch from Room if Firebase fails or client not found
            clientDao.getClientById(id)?.toDomain()
        }
    }

    override suspend fun insertClient(client: Client) {
        // client.id from domain is Int. If it's 0, it's a new client.
        // client.toEntity() will map this appropriately. Room will auto-generate ID if entity's id is 0.
        // client.toFirebase() will map domain id to String. If domain id is 0, it maps to empty string for Firebase ID.
        // The ClientFirebaseDataSource.addClient implementation needs to handle empty string ID (e.g., by letting Firestore auto-generate).
        
        withContext(Dispatchers.IO) {
            // Save to Room first. If Room auto-generates an ID for a new client,
            // we might want to use that generated ID for Firebase if consistency is key.
            // For simplicity here, we assume client.id is either pre-set (for updates) or 0 (for inserts).
            val entity = client.toEntity()
            clientDao.insertClient(entity) // Assuming OnConflictStrategy.REPLACE or similar

            // Reflect insert in Firebase
            try {
                // If client.id was 0, entity.id might still be 0 if insertClient doesn't return the ID or if not captured.
                // The toFirebase() mapper handles id=0 to id="".
                // The addClient in FirebaseDataSource should handle ID generation if it receives an empty id.
                clientFirebaseDataSource.addClient(client.toFirebase()) 
            } catch (e: Exception) {
                // Log.e("ClientRepo", "Error adding client to Firebase", e)
                // Handle Firebase error (e.g., queue for later sync, notify user)
            }
        }
    }

    override suspend fun updateClient(client: Client) {
        withContext(Dispatchers.IO) {
            clientDao.updateClient(client.toEntity())
            try {
                clientFirebaseDataSource.updateClient(client.toFirebase())
            } catch (e: Exception) {
                // Log.e("ClientRepo", "Error updating client in Firebase", e)
            }
        }
    }

    override suspend fun deleteClient(client: Client) {
        withContext(Dispatchers.IO) {
            clientDao.deleteClient(client.toEntity())
            try {
                clientFirebaseDataSource.deleteClient(client.id.toString()) // Firebase uses String ID
            } catch (e: Exception) {
                // Log.e("ClientRepo", "Error deleting client from Firebase", e)
            }
        }
    }
}
