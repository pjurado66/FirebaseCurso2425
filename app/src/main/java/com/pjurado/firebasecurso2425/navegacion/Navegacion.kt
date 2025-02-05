package com.pjurado.firebasecurso2425.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pjurado.firebasecurso2425.data.AuthManager
import com.pjurado.firebasecurso2425.data.FirestoreManager
import com.pjurado.firebasecurso2425.screen.ForgotPasswordScreen
import com.pjurado.firebasecurso2425.screen.HomeScreen
import com.pjurado.firebasecurso2425.screen.LoginScreen
import com.pjurado.firebasecurso2425.screen.SignUpScreen

@Composable
fun Navegacion(auth: AuthManager) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val firestore = FirestoreManager(auth, context)

    NavHost(navController = navController, startDestination = Login) {
        composable<Login> {
            LoginScreen(
                auth,
                { navController.navigate(SignUp) },
                {
                    navController.navigate(Home) {
                        popUpTo(Login) { inclusive = true }
                    }
                },
                { navController.navigate(ForgotPassword) }
            )
        }

        composable<SignUp> {
            SignUpScreen(
                auth
            ) { navController.popBackStack() }
        }

        composable<Home> {
            HomeScreen(
                auth,
                firestore,
                {
                    navController.navigate(Login) {
                        popUpTo(Home){ inclusive = true }
                    }
                }
            )
        }

        composable <ForgotPassword> {
            ForgotPasswordScreen(
                auth
            ) { navController.navigate(Login) {
                popUpTo(Login){ inclusive = true }
            } }
        }
    }
}