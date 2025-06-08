package com.example.gestiontienda2.domain.repository

import com.example.gestiontienda2.domain.models.Provider
import kotlinx.coroutines.flow.Flow

interface ProviderRepository {

    fun getAllProviders(): Flow<List<Provider>>

    suspend fun getProviderById(providerId: Int): Provider?

    suspend fun addProvider(provider: Provider)

    suspend fun updateProvider(provider: Provider)

    suspend fun deleteProvider(provider: Provider)
}