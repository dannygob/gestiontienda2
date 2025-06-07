package com.example.gestiontienda2.domain.usecases

import com.your_app_name.domain.models.Provider
import com.your_app_name.domain.repository.ProviderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProvidersUseCase @Inject constructor(
    private val providerRepository: ProviderRepository
) {

    operator fun invoke(): Flow<List<Provider>> {
        return providerRepository.getAllProviders()
    }
}
