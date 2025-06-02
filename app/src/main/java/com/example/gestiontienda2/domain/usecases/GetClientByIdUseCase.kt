package com.gestiontienda2.domain.usecases

import com.example.gestiontienda2.domain.models.Client
import com.gestiontienda2.domain.repository.ClientRepository
import javax.inject.Inject

class GetClientByIdUseCase @Inject constructor(
    private val clientRepository: ClientRepository
) {
    suspend operator fun invoke(clientId: Int): Client? {
        return clientRepository.getClientById(clientId)
    }
}