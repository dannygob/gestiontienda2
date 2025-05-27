package com.your_app_name.domain.usecase

import com.your_app_name.domain.models.Client
import com.your_app_name.domain.repository.ClientRepository
import javax.inject.Inject

class AddClientUseCase @Inject constructor(
    private val clientRepository: ClientRepository
) {
    suspend operator fun invoke(client: Client): Unit = clientRepository.insertClient(client)
}