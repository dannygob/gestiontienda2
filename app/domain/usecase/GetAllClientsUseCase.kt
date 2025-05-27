package com.your_app_name.domain.usecase

import com.your_app_name.domain.models.Client
import com.your_app_name.domain.repository.ClientRepository
import javax.inject.Inject

class GetAllClientsUseCase @Inject constructor(
    private val clientRepository: ClientRepository
) {
    operator fun invoke(): Flow<List<Client>> {
        return clientRepository.getAllClients()
    }import kotlinx.coroutines.flow.Flow
}