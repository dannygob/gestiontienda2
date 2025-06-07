package com.example.gestiontienda2.domain.usecases

import com.your_app_name.domain.models.Client
import com.your_app_name.domain.repository.ClientRepository
import javax.inject.Inject

class GetClientByIdUseCase @Inject constructor(
    private val clientRepository: ClientRepository
) {
    suspend operator fun invoke(clientId: Int): Client? {
        return clientRepository.getClientById(clientId)
    }
}
