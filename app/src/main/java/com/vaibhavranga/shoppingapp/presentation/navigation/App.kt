package com.vaibhavranga.shoppingapp.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vaibhavranga.shoppingapp.presentation.screens.HomeScreen
import com.vaibhavranga.shoppingapp.presentation.screens.SignInScreen
import com.vaibhavranga.shoppingapp.presentation.screens.SignUpScreen

@Composable
fun App() {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.SignInScreen,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable<Routes.SignInScreen> {
                SignInScreen(
                    onSignUpButtonClick = {
                        navController.navigate(Routes.SignUpScreen)
                    },
                    onSignInWithEmailAndPasswordSuccess = {
                        navController.navigate(Routes.HomeScreen)
                    }
                )
            }
            composable<Routes.SignUpScreen> {
                SignUpScreen(
                    onSignUpSuccessful = {
                        navController.navigate(Routes.HomeScreen)
                    }
                )
            }
            composable<Routes.HomeScreen> {
                HomeScreen()
            }
        }
    }
}