package com.vaibhavranga.shoppingapp.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Graph {
    @Serializable
    object AuthGraph : Graph()

    @Serializable
    object HomeGraph : Graph()
}

sealed class Auth {
    @Serializable
    object SignInScreenRoute : Auth()

    @Serializable
    object SignUpScreenRoute : Auth()
}

sealed class Home {
    @Serializable
    object HomeScreenRoute : Home()

    @Serializable
    object NotificationsScreenRoute : Home()

    @Serializable
    object AllCategoriesScreenRoute : Home()

    @Serializable
    object AllProductsScreenRoute : Home()

    @Serializable
    object WishListScreenRoute : Home()

    @Serializable
    object CartScreenRoute : Home()

    @Serializable
    object ProfileScreenRoute : Home()
}