package com.pjurado.firebasecurso2425.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pjurado.autenticacin.ui.screen.home.AddNoteDialog
import com.pjurado.firebasecurso2425.R
import com.pjurado.firebasecurso2425.data.AuthManager
import com.pjurado.firebasecurso2425.data.FirestoreManager
import com.pjurado.firebasecurso2425.data.model.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(auth: AuthManager, firestore: FirestoreManager, navigateToLogin: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    val user = auth.getCurrentUser()
    val factory = HomeViewModelFactory(firestore)
    val homeViewModel = viewModel(HomeViewModel::class.java, factory = factory)
    val uiState by homeViewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (user?.photoUrl != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(user?.photoUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Imagen",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(40.dp)
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.profile),
                                contentDescription = "Foto de perfil por defecto",
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )

                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = user?.displayName ?: "Anónimo",
                                fontSize = 20.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = user?.email ?: "Sin correo",
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Cyan),
                actions = {
                    IconButton(onClick = {
                        showDialog = true
                    }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ExitToApp,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { homeViewModel.onAddNoteSelected() }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (uiState.showLogoutDialog) {
                LogoutDialog(
                    onDismiss = { showDialog = false },
                    onConfirm = {
                        auth.signOut()
                        navigateToLogin()
                        showDialog = false
                    }
                )
            }

            if (uiState.showAddNoteDialog) {
                AddNoteDialog(
                    onNoteAdded = { note ->
                        homeViewModel.addNote(
                            Note(
                                id = "",
                                userId = auth.getCurrentUser()?.uid,
                                note.title ?: "",
                                note.description ?: ""
                            )
                        )
                        homeViewModel.dismisShowAddNoteDialog()
                    },
                    onDialogDismissed = { homeViewModel.dismisShowAddNoteDialog() },
                    auth = auth
                )
            }
            if (!uiState.notes.isNullOrEmpty()) {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    uiState.notes.forEach { note ->
                        item {
                            NoteItem(
                                note = note,
                                { homeViewModel.deleteNoteById(note.id ?: "") }
                            ) {
                                homeViewModel.updateNote(it)
                            }
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "Notes icon",
                        modifier = Modifier.padding(16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No se encontraro \nNotas",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Thin,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun LogoutDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cerrar Sesión") },
        text = {
            Text("¿Estás seguro de que deseas cerrar sesión?")
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
