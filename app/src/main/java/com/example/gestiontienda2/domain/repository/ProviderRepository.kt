package com.your_app_name.domain.repository

import com.your_app_name.domain.models.Provider
import kotlinx.coroutines.flow.Flow

interface ProviderRepository {

    fun getAllProviders(): Flow<List<Provider>>

    suspend fun getProviderById(providerId: Int): Provider?

    suspend fun addProvider(provider: Provider)

    suspend fun updateProvider(provider: Provider)

    suspend fun deleteProvider(provider: Provider)
}