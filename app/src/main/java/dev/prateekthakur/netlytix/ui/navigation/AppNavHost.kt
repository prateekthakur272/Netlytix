package dev.prateekthakur.netlytix.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
@androidx.annotation.RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
fun AppNavHost(controller: NavHostController = rememberNavController()) {
    CompositionLocalProvider(LocalNavHostController provides controller) {
        NavHost(navController = controller, startDestination = AppRoutes.Home.routeTemplate){
            composable(AppRoutes.Home.routeTemplate) {

            }
        }
    }
}