package com.example.appgas.Navegation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appgas.Screen.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("categorias") { CategoriaScreen(navController = navController) }
        composable("cuentas") { CuentaScreen(navController = navController) }
        composable("gastos") { GastoScreen(navController = navController) }
        composable("saldos") { SaldoScreen(navController = navController) }
        composable("transacciones") { TransaccionScreen() }
        composable("register") { RegisterScreen(navController) }
        composable("reporte_gastos") { ReporteGastosScreen() }
    }
}