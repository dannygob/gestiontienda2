package com.example.gestiontienda2.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.gestiontienda2.data.local.dao.ProviderDao
import com.example.gestiontienda2.data.local.room.entities.entity.ProviderEntity
import com.example.gestiontienda2.data.remote.firebase.datasource.source.ProviderFirebaseDataSource
import com.example.gestiontienda2.data.remote.firebase.models.ProviderFirebase
import com.example.gestiontienda2.domain.models.Provider
import com.example.gestiontienda2.domain.repository.ProviderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProviderRepositoryImpl @Inject constructor(
    private val context: Context,  // Se inyecta Context para verificar conectividad
    private val providerDao: ProviderDao,
    private val providerFirebaseDataSource: ProviderFirebaseDataSource,
) : ProviderRepository {

    override fun getAllProviders(): Flow<List<Provider>> {
        return providerDao.getAllProviders().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getProviderById(providerId: Int): Provider? {
        return if (isOnline()) {
            try {
                providerFirebaseDataSource.getProviderById(providerId.toString())?.toDomain()
                    ?.also {
                        withContext(Dispatchers.IO) {
                            providerDao.updateProvider(it.toEntity())
                        }
                    }
            } catch (e: Exception) {
                providerDao.getProviderById(providerId)?.toDomain()
            }
        } else {
            providerDao.getProviderById(providerId)?.toDomain()
        }
    }

    override suspend fun addProvider(provider: Provider) {
        withContext(Dispatchers.IO) {
            providerDao.insertProvider(provider.toEntity())
        }
        try {
            providerFirebaseDataSource.addProvider(provider.toFirebase())
        } catch (_: Exception) {
        }
    }

    override suspend fun updateProvider(provider: Provider) {
        withContext(Dispatchers.IO) {
            providerDao.updateProvider(provider.toEntity())
        }
    }

    override suspend fun deleteProvider(provider: Provider) {
        withContext(Dispatchers.IO) {
            providerDao.deleteProvider(provider.toEntity())
        }
    }

    private fun isOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}

// Mapper functions
fun ProviderEntity.toDomain(): Provider {
    return Provider(
        id = this.id,
        name = this.name,
        phone = this.phone.toString(),
        address = this.address.toString(),
        email = this.email
    )
}

fun Provider.toFirebase(): ProviderFirebase {
    return ProviderFirebase(
        id = this.id.takeIf { it != 0 }?.toString().toString(),
        name = this.name,
        phone = this.phone,
        email = this.email.toString()
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
