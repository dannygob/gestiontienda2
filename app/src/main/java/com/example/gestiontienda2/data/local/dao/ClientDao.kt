package com.gestiontienda2.data.local.dao

import androidx.room.*
import com.gestiontienda2.data.local.room.entities.ClientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(client: ClientEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllClients(clients: List<ClientEntity>)

    @Update
    suspend fun updateClient(client: ClientEntity)

    @Delete
    suspend fun deleteClient(client: ClientEntity)

    @Query("SELECT * FROM clients WHERE id = :clientId")
    suspend fun getClientById(clientId: Long): ClientEntity?

    @Query("SELECT * FROM clients")
    fun getAllClients(): Flow<List<ClientEntity>>

    // Assuming this is the blocking call used in repositories
    @Query("SELECT * FROM clients")
    fun getAllClientsBlocking(): List<ClientEntity>
}