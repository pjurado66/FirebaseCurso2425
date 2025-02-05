package com.pjurado.firebasecurso2425.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pjurado.firebasecurso2425.data.FirestoreManager
import com.pjurado.firebasecurso2425.data.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(val firestoreManager: FirestoreManager): ViewModel() {
    val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            firestoreManager.getNotes().collect { notes ->
                _uiState.update { uiState ->
                    uiState.copy(
                        notes = notes,
                        isLoading = false
                    )
                }
            }
        }
    }
    fun addNote(note: Note) {
        viewModelScope.launch {
            firestoreManager.addNote(note)
        }
    }
    fun deleteNoteById(noteId: String) {
        if (noteId.isEmpty()) return
        viewModelScope.launch {
            firestoreManager.deleteNoteById(noteId)
        }
    }
    fun updateNote(note: Note) {
        viewModelScope.launch {
            firestoreManager.updateNote(note)
        }
    }

    fun onAddNoteSelected() {
        _uiState.update { it.copy(showAddNoteDialog = true) }
    }
    fun dismisShowAddNoteDialog() {
        _uiState.update { it.copy(showAddNoteDialog = false) }
    }
    fun onLogoutSelected() {
        _uiState.update { it.copy(showLogoutDialog = true) }
    }
    fun dismisShowLogoutDialog() {
        _uiState.update { it.copy(showLogoutDialog = false) }
    }
}

data class UiState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val showAddNoteDialog: Boolean = false,
    val showLogoutDialog: Boolean = false
)

class HomeViewModelFactory(private val firestoreManager: FirestoreManager): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(firestoreManager) as T
    }
}