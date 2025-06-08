package com.example.gestiontienda2.data.remote.firebase.models

// Placeholder for Firebase Client model
// Fields should ideally mirror what's stored in Firestore for a client.
// Add a no-argument constructor for Firestore deserialization if needed,
// though data classes with default values usually work.
data class ClientFirebase(
    val id: String = "", // Firestore document ID
    val name: String = "",
    val phone: String? = null,
    val email: String? = null,
    val address: String? = null
    // Add any other fields that are specific to the Firebase representation
)
