package com.your_app_name.domain.usecase

import com.your_app_name.domain.models.Client
import com.your_app_name.domain.repository.ClientRepository
import javax.inject.Inject

class GetClientByIdUseCase @Inject constructor(
    private val clientRepository: ClientRepository // Inject ClientRepository
) {
    suspend operator fun invoke(clientId: Int): Client? {
        return clientRepository.getClientById(clientId)
    }
}