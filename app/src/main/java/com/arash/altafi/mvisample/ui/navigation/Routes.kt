package com.arash.altafi.mvisample.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    val route: String

    @Serializable
    data object Splash : Route {
        override val route: String = ".ui.navigation.Route.Splash"
    }

    @Serializable
    data object Main : Route {
        override val route: String = ".ui.navigation.Route.Main"
    }

    @Serializable
    data object User : Route {
        override val route: String = ".ui.navigation.Route.User"
    }

    @Serializable
    data object Celebrity : Route {
        override val route: String = ".ui.navigation.Route.Celebrity"
    }

    @Serializable
    data class ImageScreen(var title: String, val imageUrl: String) : Route {
        override val route: String = ".ui.navigation.Route.ImageScreen"
    }
}