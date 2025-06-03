package com.gestiontienda2.domain.usecases

import com.example.gestiontienda2.domain.models.Provider
import com.gestiontienda2.domain.repository.ProviderRepository
import javax.inject.Inject

class GetProviderByIdUseCase @Inject constructor(
    private val providerRepository: ProviderRepository
) {
    suspend operator fun invoke(providerId: Int): Provider? {
        return providerRepository.getProviderById(providerId)
    }
}