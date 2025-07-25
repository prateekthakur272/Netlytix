package dev.prateekthakur.netlytix.ui.navigation

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

val LocalNavHostController = staticCompositionLocalOf <NavHostController>{
    error("Not initialized")
}