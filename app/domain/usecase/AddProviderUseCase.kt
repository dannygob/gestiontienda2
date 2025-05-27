package com.your_app_name.domain.usecase

import com.your_app_name.domain.models.Provider
import com.your_app_name.domain.repository.ProviderRepository
import javax.inject.Inject

class AddProviderUseCase @Inject constructor(
    private val providerRepository: ProviderRepository
) {
    suspend operator fun invoke(provider: Provider) {
        providerRepository.addProvider(provider)
    }
}