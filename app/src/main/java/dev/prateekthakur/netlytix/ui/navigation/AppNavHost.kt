package dev.prateekthakur.netlytix.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.prateekthakur.netlytix.data.repository.NetworkRepositoryImpl
import dev.prateekthakur.netlytix.ui.features.networkinfo.NetworkInfoScreen
import kotlinx.coroutines.launch

@Composable
@androidx.annotation.RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
fun AppNavHost(modifier: Modifier = Modifier, controller: NavHostController = rememberNavController()) {
    CompositionLocalProvider(LocalNavHostController provides controller) {
        NavHost(navController = controller, startDestination = AppRoutes.Home.routeTemplate){
            composable(AppRoutes.Home.routeTemplate) {
                NetworkInfoScreen()
            }
        }
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = NetworkRepositoryImpl(context)

    LaunchedEffect(Unit) {
        scope.launch  {
            Log.d("AppNavHost", "getConnectionType: ${repository.getConnectionType()}")
            Log.d("AppNavHost", "getIpv4Address: ${repository.getIpv4Address()}")
            Log.d("AppNavHost", "getIpv6Address: ${repository.getIpv6Address()}")
            Log.d("AppNavHost", "getWifiSSID: ${repository.getWifiSSID()}")
            Log.d("AppNavHost", "getWifiBSSID: ${repository.getWifiBSSID()}")
            Log.d("AppNavHost", "getGatewayAddress: ${repository.getGatewayAddress()}")
            Log.d("AppNavHost", "getDnsServers: ${repository.getDnsServers()}")
            Log.d("AppNavHost", "getMacAddress: ${repository.getMacAddress()}")
            Log.d("AppNavHost", "getSignalStrength: ${repository.getSignalStrength()}")
            Log.d("AppNavHost", "getLinkSpeedMbps: ${repository.getLinkSpeedMbps()}")
            Log.d("AppNavHost", "getPingLatencyMs: ${repository.getPingLatencyMs()}")
            Log.d("AppNavHost", "getPacketLossPercent: ${repository.getPacketLossPercent()}")
            Log.d("AppNavHost", "isInternetAccessible: ${repository.isInternetAccessible()}")
            Log.d("AppNavHost", "isVpnActive: ${repository.isVpnActive()}")
            Log.d("AppNavHost", "getVpnType: ${repository.getVpnType()}")
            Log.d("AppNavHost", "getPublicIpAddress: ${repository.getPublicIpAddress()}")
            Log.d("AppNavHost", "getDownloadSpeedKbps: ${repository.getDownloadSpeedKbps()}")
            Log.d("AppNavHost", "getUploadSpeedKbps: ${repository.getUploadSpeedKbps()}")
        }
    }
}