package com.inventoryapp.domain.usecases

import com.your_app_name.domain.models.Client
import com.inventoryapp.domain.repository.ClientRepository
import javax.inject.Inject

class AddClientUseCase @Inject constructor(
    private val clientRepository: ClientRepository
) {
    suspend operator fun invoke(client: Client) {
        clientRepository.addClient(client)
    }
}
