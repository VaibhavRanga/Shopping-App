package com.vaibhavranga.shoppingapp.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.vaibhavranga.shoppingapp.presentation.screens.auth.SignInScreen
import com.vaibhavranga.shoppingapp.presentation.screens.auth.SignUpScreen

fun NavGraphBuilder.authNavGraph(rootNavController: NavHostController) {
    navigation<Graph.AuthGraph>(startDestination = Auth.SignInScreenRoute) {
        composable<Auth.SignInScreenRoute> {
            SignInScreen(
                onSignUpButtonClick = {
                    rootNavController.navigate(Auth.SignUpScreenRoute)
                },
                onSignInWithEmailAndPasswordSuccess = {
                    rootNavController.navigate(Graph.HomeGraph) {
                        popUpTo<Graph.AuthGraph> {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<Auth.SignUpScreenRoute> {
            SignUpScreen(
                onSignUpSuccessful = {
                    rootNavController.navigate(Graph.HomeGraph) {
                        popUpTo<Graph.AuthGraph> {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}