package com.your_app_name.domain.usecase

import com.your_app_name.domain.models.Provider
import com.your_app_name.domain.repository.ProviderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllProvidersUseCase @Inject constructor(
    private val providerRepository: ProviderRepository
) {
    operator fun invoke(): Flow<List<Provider>> {
        return providerRepository.getAllProviders()
    }
}