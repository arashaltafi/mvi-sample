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
    data object Main2 : Route {
        override val route: String = ".ui.navigation.Route.Main2"
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
    data object Paging : Route {
        override val route: String = ".ui.navigation.Route.Paging"
    }

    @Serializable
    data class ImageScreen(var title: String, val imageUrl: String) : Route {
        override val route: String = ".ui.navigation.Route.ImageScreen"
    }

    @Serializable
    data object TestList : Route {
        override val route: String = ".ui.navigation.Route.TestList"
    }

    @Serializable
    data object TestPagingList : Route {
        override val route: String = ".ui.navigation.Route.TestPagingList"
    }

    @Serializable
    data class TestDetail(var userId: String) : Route {
        override val route: String = ".ui.navigation.Route.TestDetail"
    }
}