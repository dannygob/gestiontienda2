package com.your_app_name.data.local.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.your_app_name.data.local.room.entities.ClientEntity // Assuming ClientEntity exists

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
    suspend fun getClientById(clientId: Int): ClientEntity?

    @Query("SELECT * FROM clients")
    fun getAllClients(): Flow<List<ClientEntity>>

    // Assuming this is the blocking call used in repositories
    @Query("SELECT * FROM clients")
    fun getAllClientsBlocking(): List<ClientEntity>
}