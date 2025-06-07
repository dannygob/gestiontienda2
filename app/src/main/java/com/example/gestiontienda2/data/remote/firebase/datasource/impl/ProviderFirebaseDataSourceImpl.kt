package com.example.gestiontienda2.data.remote.firebase.datasource.impl

import com.example.gestiontienda2.data.remote.firebase.datasource.source.ProviderFirebaseDataSource
import com.example.gestiontienda2.data.remote.firebase.models.ProviderFirebase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton

class ProviderFirebaseDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProviderFirebaseDataSource {


    private val providersCollection = firestore.collection("providers")

    override fun getAllProviders(): Flow<List<ProviderFirebase>> = flow {
        val snapshot = providersCollection.get().await()
        val providers = snapshot.documents.mapNotNull { it.toObject(ProviderFirebase::class.java) }
        emit(providers)
    }.catch { e ->
        emit(emptyList()) // Emit empty list on error
    }

    override suspend fun getProviderById(providerId: String): ProviderFirebase? {
        return try {
            providersCollection.document(providerId).get().await()
                .toObject(ProviderFirebase::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun addProvider(provider: ProviderFirebase) {
        try {
            providersCollection.document(provider.id).set(provider).await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updateProvider(provider: ProviderFirebase) {
        try {
            providersCollection.document(provider.id).set(provider).await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteProvider(provider: ProviderFirebase) {
        try {
            providersCollection.document(provider.id).delete().await()
        } catch (e: Exception) {
            throw e
        }
    }
}