package com.gestiontienda2.domain.usecases

import com.gestiontienda2.domain.models.Provider
import com.gestiontienda2.domain.repository.ProviderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProvidersUseCase @Inject constructor(
    private val providerRepository: ProviderRepository
) {

    operator fun invoke(): Flow<List<Provider>> {
        return providerRepository.getAllProviders()
    }
}