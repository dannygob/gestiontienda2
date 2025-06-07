package com.example.gestiontienda2.domain.usecases

import com.your_app_name.domain.models.Client
import com.your_app_name.domain.repository.ClientRepository
import javax.inject.Inject

class UpdateClientUseCase @Inject constructor(
    private val clientRepository: ClientRepository
) {
    suspend operator fun invoke(client: Client) {
        clientRepository.updateClient(client)
    }
}
