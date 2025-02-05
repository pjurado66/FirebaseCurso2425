package com.pjurado.firebasecurso2425.data

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.pjurado.firebasecurso2425.data.model.Note
import com.pjurado.firebasecurso2425.data.model.NoteDB
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirestoreManager(auth: AuthManager, context: Context) {
    private val firestore = FirebaseFirestore.getInstance()
    private val userId = auth.getCurrentUser()?.uid

    companion object {
        const val NOTE_COLLECTION = "Notas"
    }

    fun getNotes(): Flow<List<Note>> {
        return firestore.collection(NOTE_COLLECTION)
            .whereEqualTo("userId", userId)
            .snapshots()
            .map { qs ->
                qs.documents.mapNotNull { ds ->
                    ds.toObject(NoteDB::class.java)?.let { noteDB ->
                        Note(
                            id = ds.id,
                            userId = noteDB.userId,
                            title = noteDB.title,
                            description = noteDB.description
                        )
                    }
                }
            }
    }

    suspend fun addNote(note: Note){
        firestore.collection(NOTE_COLLECTION).add(note).await()
    }
    suspend fun updateNote(note: Note) {
        val noteRef = note.id?.let {
            firestore.collection(NOTE_COLLECTION).document(it)
        }
        noteRef?.set(note)?.await()
    }
    suspend fun deleteNoteById(noteId: String) {
        firestore.collection(NOTE_COLLECTION).document(noteId).delete().await()
    }
    fun getNoteId(id: String): Note {
        return firestore.collection(NOTE_COLLECTION).document(id)
            .get().result?.toObject(NoteDB::class.java)?.let {
                Note(
                    id = id,
                    userId = it.userId,
                    title = it.title,
                    description = it.description
                )
            }!!
    }

}