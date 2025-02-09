package com.vaibhavranga.shoppingapp.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Routes {
    @Serializable
    object SignInScreen : Routes()

    @Serializable
    object SignUpScreen : Routes()

    @Serializable
    object HomeScreen : Routes()
}