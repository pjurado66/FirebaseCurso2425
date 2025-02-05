package com.pjurado.firebasecurso2425.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pjurado.autenticacin.ui.screen.home.DeleteNoteDialog
import com.pjurado.autenticacin.ui.screen.home.UpdateNoteDialog
import com.pjurado.firebasecurso2425.data.model.Note

@Composable
fun NoteItem(
    note: Note,
    onDeleteClick: () -> Unit,
    onUpdateNote: (Note) -> Unit
) {
    var showDeleteNoteDialog by remember { mutableStateOf(false) }
    var showUpdateNoteDialog by remember { mutableStateOf(false) }

    if (showDeleteNoteDialog) {
        DeleteNoteDialog(
            onConfirmDelete = {
                onDeleteClick()
                showDeleteNoteDialog = false
            },
            onDismiss = { showDeleteNoteDialog = false }
        )
    }

    if(showUpdateNoteDialog){
        UpdateNoteDialog(
            note = note,
            onNoteUpdated = { note ->
                onUpdateNote(note)
                showUpdateNoteDialog = false
            },
            onDialogDismissed = { showUpdateNoteDialog = false }
        )
    }

    Card(modifier = Modifier.padding(6.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = note.title ?: "",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.description ?: "",
                fontWeight = FontWeight.Thin,
                fontSize = 13.sp,
                lineHeight = 15.sp
            )
            Row {
                IconButton(
                        onClick = { showDeleteNoteDialog = true }
                        ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete note",
                    )
                }
                IconButton(
                    onClick = { showUpdateNoteDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Update note",
                    )
                }
            }
        }
    }
}
