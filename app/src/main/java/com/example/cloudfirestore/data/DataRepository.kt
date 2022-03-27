package com.example.cloudfirestore.data

import android.util.Log
import com.example.cloudfirestore.data.model.User
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class DataRepository {

    private val firestoreColUser = Firebase.firestore.collection("users")

    suspend fun addUser(user: User){
        val id = firestoreColUser.document().id
        user.id = id
        firestoreColUser.document(id).set(user).await()
    }

    fun getUsers(
        search:String
    ):Flow<List<User>> = callbackFlow {
        val userList = firestoreColUser
                /**
                 * order by*/
//            .whereEqualTo("username", "username")
//            .whereGreaterThan("age", (0..100).random())
//            .whereLessThan("age", (101..1000).random())
            .addSnapshotListener { value, e ->
                e?.let {
                    Log.e("Firestore", it.message.toString())
                    return@addSnapshotListener
                }
                value?.let {
                    val users = ArrayList<User>()
                    for(document in it) {
                        val user = document.toObject<User>()
                        Log.e("Firestore", user.toString())
                        users.add(user)
                    }
                    trySend(users)
                }
        }
        awaitClose {
            userList.remove()
        }
    }

    suspend fun updateUser(user: User){
        firestoreColUser.document(user.id).update(
            user.getMap()
        ).await()
    }

    suspend fun updateUsername(
        username:String,
        document:String
    ){
        Firebase.firestore.runTransaction { transaction ->
            val userDocumented = firestoreColUser.document(document)
            transaction.update(userDocumented, "username", username)
            null
        }.await()
    }

    suspend fun deleteUser(user: User){
        firestoreColUser.document(user.id).delete().await()
    }
}