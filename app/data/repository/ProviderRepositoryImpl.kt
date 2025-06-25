package com.your_app_name.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.Singleton
import com.your_app_name.data.local.dao.ProviderDao
import com.your_app_name.data.local.entities.ProviderEntity
import com.your_app_name.data.remote.firebase.datasource.ProviderFirebaseDataSource
import com.your_app_name.data.remote.firebase.models.ProviderFirebase
import com.your_app_name.domain.models.Provider
import com.your_app_name.domain.repository.ProviderRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProviderRepositoryImpl @Inject constructor(
    private val providerDao: ProviderDao,
    private val providerFirebaseDataSource: ProviderFirebaseDataSource,
    @ApplicationContext private val appContext: Context // Inject ApplicationContext
) : ProviderRepository {

    override fun getAllProviders(): Flow<List<Provider>> {
        return providerDao.getAllProviders().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getProviderById(providerId: Int): Provider? {
        // Prioritize fetching from Firebase if online, otherwise get from Room
        return if (isOnline) {
            val firebaseProvider = providerFirebaseDataSource.getProviderById(providerId.toString()) // Assuming Firebase ID is String
            firebaseProvider?.toDomain()?.also { provider ->
                // Optionally update Room with the fetched data
                providerDao.updateProvider(provider.toEntity())
            }
        } else {
            providerDao.getProviderById(providerId)?.toDomain()
        }
    }

    override suspend fun addProvider(provider: Provider) {
        // Save to Room and Firebase
        providerFirebaseDataSource.addProvider(provider.toFirebase())
        providerDao.insertProvider(provider.toEntity())
    }

    override suspend fun updateProvider(provider: Provider) {
        providerDao.updateProvider(provider.toEntity())
    }

    override suspend fun deleteProvider(provider: Provider) {
        providerDao.deleteProvider(provider.toEntity())
    }
}

// Check network connectivity
private val isOnline: Boolean
    get() {
        val connectivityManager =
            appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

// Mapper functions (should ideally be in a separate mapper module or file)
fun ProviderEntity.toDomain(): Provider {
    return Provider(
        id = this.id,
        name = this.name,
        phone = this.phone,
        address = this.address,
        email = this.email
    )
}

// Add mapping function for Firebase
fun Provider.toFirebase(): ProviderFirebase {
    return ProviderFirebase(
        id = if (this.id == 0) null else this.id.toString(), // Firebase will generate ID if 0
        name = this.name,
        phone = this.phone,
        email = this.email
    )
}

fun Provider.toEntity(): ProviderEntity {
    return ProviderEntity(
        id = this.id,
        name = this.name,
        phone = this.phone,
        address = this.address,
        email = this.email
    )
}