package com.mobileinventors.domain.usecases

import com.mobileinventors.domain.model.Client
import com.mobileinventors.domain.repository.ClientRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetClientsUseCase @Inject constructor(
    private val clientRepository: ClientRepository
) {
    operator fun invoke(): Flow<List<Client>> {
        return clientRepository.getClients()
    }
}