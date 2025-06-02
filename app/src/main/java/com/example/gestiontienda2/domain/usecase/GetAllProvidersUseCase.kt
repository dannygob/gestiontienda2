package com.gestiontienda2.domain.usecase

import com.gestiontienda2.domain.models.Provider
import com.gestiontienda2.domain.repository.ProviderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllProvidersUseCase @Inject constructor(
    private val providerRepository: ProviderRepository
) {
    operator fun invoke(): Flow<List<Provider>> {
        return providerRepository.getAllProviders()
    }
}