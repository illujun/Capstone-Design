package com.example.findpill

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.findpill.ui.screen.MainScreen
import com.example.findpill.ui.screen.DetailScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
){
    NavHost(navController = navController, startDestination = "main"){
        composable("main") { MainScreen(navController) }
        composable("detail") { DetailScreen(navController) }
    }
}