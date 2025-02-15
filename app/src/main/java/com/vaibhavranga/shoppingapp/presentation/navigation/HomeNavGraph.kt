package com.vaibhavranga.shoppingapp.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.vaibhavranga.shoppingapp.presentation.screens.home.AllCategoriesScreen
import com.vaibhavranga.shoppingapp.presentation.screens.home.AllProductsByCategoryScreen
import com.vaibhavranga.shoppingapp.presentation.screens.home.CartScreen
import com.vaibhavranga.shoppingapp.presentation.screens.home.HomeScreen
import com.vaibhavranga.shoppingapp.presentation.screens.home.NotificationsScreen
import com.vaibhavranga.shoppingapp.presentation.screens.home.ProfileScreen
import com.vaibhavranga.shoppingapp.presentation.screens.home.WishListScreen
import com.vaibhavranga.shoppingapp.presentation.viewModel.ViewModel

@Composable
fun HomeNavGraph(
    rootNavController: NavHostController,
    homeNavController: NavHostController,
    viewModel: ViewModel = hiltViewModel(),
    innerPadding: PaddingValues
) {
    NavHost(
        navController = homeNavController,
        startDestination = Graph.HomeGraph,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        navigation<Graph.HomeGraph>(startDestination = Home.HomeScreenRoute) {
            composable<Home.HomeScreenRoute> {
                HomeScreen(
                    onNotificationsButtonClick = {
                        homeNavController.navigate(Home.NotificationsScreenRoute)
                    },
                    onCategoryClick = {
                        homeNavController.navigate(Home.AllProductsByCategoryScreenRoute(categoryName = it))
                    },
                    onSeeMoreCategoriesClick = {
                        homeNavController.navigate(Home.AllCategoriesScreenRoute)
                    }
                )
            }
            composable<Home.NotificationsScreenRoute> {
                NotificationsScreen()
            }
            composable<Home.AllCategoriesScreenRoute> {
                AllCategoriesScreen()
            }
            composable<Home.AllProductsByCategoryScreenRoute> {
                val categoryName = it.toRoute<Home.AllProductsByCategoryScreenRoute>().categoryName
                AllProductsByCategoryScreen(categoryName = categoryName)
            }
            composable<Home.WishListScreenRoute> {
                WishListScreen()
            }
            composable<Home.CartScreenRoute> {
                CartScreen()
            }
            composable<Home.ProfileScreenRoute> {
                ProfileScreen(
                    onSignOutButtonClick = {
                        viewModel.signOut()
                        rootNavController.navigate(Graph.AuthGraph) {
                            popUpTo<Graph.HomeGraph> {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}