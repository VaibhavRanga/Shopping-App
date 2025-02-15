package com.vaibhavranga.shoppingapp.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
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
            NavigationBar {
                val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                bottomNavigationItemsList.forEach { navigationItem ->
                    NavigationBarItem(
                        icon = {
                                Icon(
                                    imageVector = navigationItem.icon,
                                    contentDescription = navigationItem.title
                                )
                        },
                        label = { Text(navigationItem.title) },
                        selected = currentDestination?.hierarchy?.any { it.hasRoute(navigationItem.route::class.toString(), arguments = null) } == true,
                        onClick = {
                            homeNavController.navigate(navigationItem.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(homeNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
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