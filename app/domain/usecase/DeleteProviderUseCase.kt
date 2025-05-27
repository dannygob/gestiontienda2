package com.your_app_name.domain.usecase

import com.your_app_name.domain.models.Provider
import com.your_app_name.domain.repository.ProviderRepository
import javax.inject.Inject

class DeleteProviderUseCase @Inject constructor(
    private val providerRepository: ProviderRepository
) {
    operator fun invoke(provider: Provider): suspend fun Unit {
        providerRepository.deleteProvider(provider)
    }
}