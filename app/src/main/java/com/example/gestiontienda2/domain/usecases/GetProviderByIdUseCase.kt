package com.your_app_name.domain.usecases

import com.your_app_name.domain.models.Provider
import com.your_app_name.domain.repository.ProviderRepository
import javax.inject.Inject

class GetProviderByIdUseCase @Inject constructor(
    private val providerRepository: ProviderRepository
) {
    suspend operator fun invoke(providerId: Int): Provider? {
        return providerRepository.getProviderById(providerId)
    }
}