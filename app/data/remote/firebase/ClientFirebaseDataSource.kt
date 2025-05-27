package com.your_app_name.data.remote.firebase

import com.your_app_name.data.remote.firebase.ClientFirebase
import com.google.firebase.firestore.FirebaseFirestore // Assuming you'll inject this

interface ClientFirebaseDataSource {
    suspend fun getClients(): List<ClientFirebase>
    suspend fun getClientById(clientId: String): ClientFirebase?
    suspend fun addClient(client: ClientFirebase)
    suspend fun updateClient(client: ClientFirebase)
    suspend fun deleteClient(clientId: String)
}