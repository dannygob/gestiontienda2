package com.gestiontienda2.domain.usecases

import com.gestiontienda2.domain.models.Provider
import com.gestiontienda2.domain.repository.ProviderRepository
import javax.inject.Inject

class UpdateProviderUseCase @Inject constructor(
    private val providerRepository: ProviderRepository
) {
    suspend operator fun invoke(provider: Provider) {
        providerRepository.updateProvider(provider)
    }
}