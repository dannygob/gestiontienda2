package com.example.app.data.remote.firebase

import com.example.app.data.remote.firebase.ProviderFirebase

interface ProviderFirebaseDataSource {
    suspend fun getProviders(): List<ProviderFirebase>
    suspend fun getProviderById(providerId: String): ProviderFirebase?
    suspend fun addProvider(provider: ProviderFirebase)
    suspend fun updateProvider(provider: ProviderFirebase)
    suspend fun deleteProvider(providerId: String)
}