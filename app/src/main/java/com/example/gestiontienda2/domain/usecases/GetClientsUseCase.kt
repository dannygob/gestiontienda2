package com.example.gestiontienda2.domain.usecases

import com.example.gestiontienda2.domain.models.Client
import com.example.gestiontienda2.domain.repository.ClientRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetClientsUseCase @Inject constructor(
    private val clientRepository: ClientRepository
) {
    suspend operator fun invoke(): Flow<List<Client>> {
        return clientRepository.getClients()
    }
}