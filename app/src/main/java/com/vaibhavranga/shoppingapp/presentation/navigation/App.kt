package com.vaibhavranga.shoppingapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun App(
    firebaseAuth: FirebaseAuth
) {
    val rootNavController = rememberNavController()
    val startScreen = remember {
        if (firebaseAuth.currentUser == null) {
            Graph.AuthGraph
        } else {
            Graph.HomeGraph
        }
    }

    NavHost(
        navController = rootNavController,
        startDestination = startScreen
    ) {
        authNavGraph(
            rootNavController = rootNavController
        )
        composable<Graph.HomeGraph> {
            HomeNavigation(
                rootNavController = rootNavController
            )
        }
    }
}