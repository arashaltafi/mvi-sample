package com.arash.altafi.mvisample.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.arash.altafi.mvisample.ui.component.ImageScreen
import com.arash.altafi.mvisample.ui.presentation.celebrity.CelebrityScreen
import com.arash.altafi.mvisample.ui.presentation.main.MainScreen
import com.arash.altafi.mvisample.ui.presentation.user.UserScreen
import com.arash.altafi.mvisample.ui.theme.MVISampleTheme

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    MVISampleTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Route.Main,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable<Route.Main> {
                    MainScreen(navController)
                }
                composable<Route.User> {
                    UserScreen(navController)
                }
                composable<Route.Celebrity> {
                    CelebrityScreen(navController)
                }
                composable<Route.ImageScreen> { backStackEntry: NavBackStackEntry ->
                    val args = backStackEntry.toRoute<Route.ImageScreen>()
                    ImageScreen(navController, args.title, args.imageUrl)
                }
            }
        }
    }
}