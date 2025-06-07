package com.example.gestiontienda2.domain.usecases

import com.your_app_name.domain.models.Client
import com.your_app_name.domain.repository.ClientRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetClientsUseCase @Inject constructor(
    private val clientRepository: ClientRepository
) {
    operator fun invoke(): Flow<List<Client>> {
        return clientRepository.getClients()
    }
}
