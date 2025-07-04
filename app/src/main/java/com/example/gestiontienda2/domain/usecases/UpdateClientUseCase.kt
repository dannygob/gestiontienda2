package com.example.gestiontienda2.domain.usecases

import com.example.gestiontienda2.domain.models.Client
import com.example.gestiontienda2.domain.repository.ClientRepository
import javax.inject.Inject

class UpdateClientUseCase @Inject constructor(
    private val clientRepository: ClientRepository
) {
    suspend operator fun invoke(client: Client) {
        clientRepository.updateClient(client)
    }
}