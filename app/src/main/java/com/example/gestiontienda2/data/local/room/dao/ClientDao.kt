package com.example.gestiontienda2.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gestiontienda2.data.local.room.entities.entity.ClientEntity
import com.example.gestiontienda2.data.local.room.entities.entity.ServiceExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(client: ServiceExpenseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllClients(clients: List<R>)

    @Update
    suspend fun updateClient(client: ServiceExpenseEntity)

    @Delete
    suspend fun deleteClient(client: ServiceExpenseEntity)

    @Query("SELECT * FROM clients WHERE id = :clientId")
    suspend fun getClientById(clientId: Int): ClientEntity?

    @Query("SELECT * FROM clients")
    fun getAllClients(): Flow<List<ClientEntity>>

    // Assuming this is the blocking call used in repositories
    @Query("SELECT * FROM clients")
    fun getAllClientsBlocking(): List<ClientEntity>
}