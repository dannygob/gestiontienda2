package com.example.gestiontienda2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gestiontienda2.data.local.entities.entity.ProviderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProviderDao {

    @Query("SELECT * FROM providers")
    fun getAllProviders(): Flow<List<ProviderEntity>>

    @Query("SELECT * FROM providers WHERE id = :providerId")
    suspend fun getProviderById(providerId: Long): ProviderEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProvider(provider: ProviderEntity): Long

    @Update
    suspend fun updateProvider(provider: ProviderEntity)

    @Delete
    suspend fun deleteProvider(provider: ProviderEntity)
}