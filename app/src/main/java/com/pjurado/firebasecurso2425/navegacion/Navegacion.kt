package com.pjurado.firebasecurso2425.ui.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pjurado.autenticacin.ui.screen.SignUpScreen
import com.pjurado.firebasecurso2425.data.AuthManager
import com.pjurado.firebasecurso2425.navegacion.ForgotPassword
import com.pjurado.firebasecurso2425.navegacion.Home
import com.pjurado.firebasecurso2425.navegacion.Login
import com.pjurado.firebasecurso2425.navegacion.SignUp
import com.pjurado.firebasecurso2425.screen.ForgotPasswordScreen
import com.pjurado.firebasecurso2425.screen.LoginScreen

@Composable
fun Navegacion() {
    val navController = rememberNavController()
    val auth = AuthManager()

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
                {
                    navController.navigate(Login) {
                        popUpTo(Login){ inclusive = true }
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