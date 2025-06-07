package com.example.gestiontienda2.data.remote.firebase.datasource.source

import com.example.gestiontienda2.data.remote.firebase.models.ClientFirebase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ClientFirebaseDataSource {

    private val firestore = FirebaseFirestore.getInstance()
    private val clientsCollection = firestore.collection("clients")

    suspend fun getClients(): List<ClientFirebase> {
        return try {
            val snapshot = clientsCollection.get().await()
            snapshot.documents.mapNotNull { it.toObject(ClientFirebase::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getClientById(id: String): ClientFirebase? {
        return try {
            val doc = clientsCollection.document(id).get().await()
            if (doc.exists()) doc.toObject(ClientFirebase::class.java) else null
        } catch (e: Exception) {
            null
        }
    }

    // Aquí la generación automática de ID si no tiene uno
    suspend fun addClient(client: ClientFirebase): ClientFirebase {
        val id = if (client.id.isBlank()) clientsCollection.document().id else client.id
        val clientWithId = client.copy(id = id)
        clientsCollection.document(id).set(clientWithId).await()
        return clientWithId
    }

    suspend fun updateClient(client: ClientFirebase) {
        try {
            clientsCollection.document(client.id).set(client).await()
        } catch (e: Exception) {
            // Manejar error aquí si quieres
        }
    }

    suspend fun deleteClient(id: String) {
        try {
            clientsCollection.document(id).delete().await()
        } catch (e: Exception) {
            // Manejar error aquí si quieres
        }
    }
}
