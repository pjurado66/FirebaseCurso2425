package com.pjurado.firebasecurso2425

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.pjurado.firebasecurso2425.data.AuthManager
import com.pjurado.firebasecurso2425.navegacion.Navegacion
import com.pjurado.firebasecurso2425.ui.theme.FirebaseCurso2425Theme

class MainActivity : ComponentActivity() {
    val auth = AuthManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Firebase.analytics
        setContent {
            FirebaseCurso2425Theme {
                Navegacion(auth)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        auth.signOut()
    }
}
