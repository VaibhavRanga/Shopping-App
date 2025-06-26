package com.vaibhavranga.shoppingapp.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeNavigation(
    rootNavController: NavHostController,
    homeNavController: NavHostController = rememberNavController()
) {
    val bottomNavigationItemsList = listOf(
        BottomNavigationItem(
            title = "Home",
            route = Home.HomeScreenRoute,
            icon = Icons.Outlined.Home
        ),
        BottomNavigationItem(
            title = "Wish List",
            route = Home.WishListScreenRoute,
            icon = Icons.Outlined.FavoriteBorder
        ),
        BottomNavigationItem(
            title = "Cart",
            route = Home.CartScreenRoute,
            icon = Icons.Outlined.ShoppingCart
        ),
        BottomNavigationItem(
            title = "Profile",
            route = Home.ProfileScreenRoute,
            icon = Icons.Outlined.Person
        )
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            NavigationBar {
                bottomNavigationItemsList.forEachIndexed { index, item ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == item.route::class.qualifiedName } == true
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title) },
                        selected = isSelected,
                        onClick = {
                            homeNavController.navigate(item.route) {
                                popUpTo(homeNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        HomeNavGraph(
            rootNavController = rootNavController,
            homeNavController = homeNavController,
            innerPadding = innerPadding
        )
    }
}

data class BottomNavigationItem(
    val title: String,
    val route: Home,
    val icon: ImageVector
)