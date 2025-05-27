package com.your_app_name.data.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.your_app_name.domain.models.Client
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ClientFirebaseDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ClientFirebaseDataSource {

    override suspend fun getClients(): List<ClientFirebase> {
        return firestore.collection("clients")
            .get()
            .await()
            .toObjects(ClientFirebase::class.java)
    }

    override suspend fun getClientById(clientId: String): ClientFirebase? {
        return firestore.collection("clients")
            .document(clientId)
            .get()
            .await()
            .toObject(ClientFirebase::class.java)
    }

    override suspend fun addClient(client: ClientFirebase): String {
        val documentReference = firestore.collection("clients").add(client).await()
        return documentReference.id
    }

    override suspend fun updateClient(client: ClientFirebase) {
        client.id?.let { id ->
            firestore.collection("clients").document(id).set(client).await()
        }
    }

    override suspend fun deleteClient(clientId: String) {
        firestore.collection("clients").document(clientId).delete().await()
    }
}