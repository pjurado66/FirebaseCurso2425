package com.pjurado.autenticacin.ui.screen.home

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DeleteNoteDialog(onConfirmDelete: () -> Unit, onDismiss: () -> Unit){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete note") },
        text = { Text("Are you sure you want to delete this note?") },
        confirmButton = {
            Button(
                onClick = onConfirmDelete
            ) {
                Text("Accept")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}