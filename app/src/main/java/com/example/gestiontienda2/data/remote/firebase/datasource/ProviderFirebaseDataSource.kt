package com.example.gestiontienda2.data.remote.firebase.datasource

import com.example.gestiontienda2.data.remote.firebase.models.ProviderFirebase
import kotlinx.coroutines.flow.Flow

interface ProviderFirebaseDataSource {

    fun getAllProviders(): Flow<List<ProviderFirebase>>

    suspend fun getProviderById(providerId: String): ProviderFirebase?

    suspend fun addProvider(provider: ProviderFirebase)

    suspend fun updateProvider(provider: ProviderFirebase)

    suspend fun deleteProvider(provider: ProviderFirebase)
}