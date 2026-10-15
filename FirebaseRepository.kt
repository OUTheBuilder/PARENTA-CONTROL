package com.parentguard.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.parentguard.util.Role
import kotlinx.coroutines.tasks.await

/**
 * Single access point for auth + Firestore. Keeping this separate from the
 * UI layer means the native Activities never touch the SDK directly.
 */
class FirebaseRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    val currentUserId: String? get() = auth.currentUser?.uid

    suspend fun register(email: String, password: String, role: Role, displayName: String): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw IllegalStateException("No UID returned")
            val profile = hashMapOf(
                "email" to email,
                "role" to role.value,
                "displayName" to displayName,
                "createdAt" to System.currentTimeMillis()
            )
            db.collection("users").document(uid).set(profile).await()
            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user?.uid.orEmpty())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRole(uid: String): Role {
        return try {
            val doc = db.collection("users").document(uid).get().await()
            Role.from(doc.getString("role"))
        } catch (e: Exception) {
            Role.CHILD
        }
    }

    fun logout() = auth.signOut()
}
