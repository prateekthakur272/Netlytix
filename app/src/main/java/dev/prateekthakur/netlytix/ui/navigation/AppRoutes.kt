package dev.prateekthakur.netlytix.ui.navigation

sealed class AppRoutes(val routeTemplate: String) {
    abstract val route : String

    data object Home: AppRoutes("home") {
        override val route: String
            get() = routeTemplate
    }
}