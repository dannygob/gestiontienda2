package com.example.gestiontienda2.domain.usecases

import com.your_app_name.domain.models.Provider
import com.your_app_name.domain.repository.ProviderRepository
import javax.inject.Inject

class UpdateProviderUseCase @Inject constructor(
    private val providerRepository: ProviderRepository
) {
    suspend operator fun invoke(provider: Provider) {
        providerRepository.updateProvider(provider)
    }
}
