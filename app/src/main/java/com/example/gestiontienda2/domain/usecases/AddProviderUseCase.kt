package com.example.gestiontienda2.domain.usecases

import com.example.gestiontienda2.domain.models.Provider
import com.example.gestiontienda2.domain.repository.ProviderRepository
import javax.inject.Inject

class AddProviderUseCase @Inject constructor(
    private val providerRepository: ProviderRepository
) {
    suspend operator fun invoke(provider: Provider) {
        providerRepository.addProvider(provider)
    }
}