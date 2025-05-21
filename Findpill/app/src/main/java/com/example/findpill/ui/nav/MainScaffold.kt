package com.example.findpill.ui.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.findpill.ui.component.BottomNavigationBar
import com.example.findpill.ui.component.DetailBottomNavigationBar
import com.example.findpill.ui.screen.Album
import com.example.findpill.ui.screen.Confirm
import com.example.findpill.ui.screen.Confirm2
import com.example.findpill.ui.screen.DetailScreen
import com.example.findpill.ui.screen.Loading
import com.example.findpill.ui.screen.MainScreen
import com.example.findpill.ui.screen.PhotoSearch
import com.example.findpill.ui.screen.Photoing
import com.example.findpill.ui.screen.PillCalendar
import com.example.findpill.ui.screen.Result
import com.example.findpill.ui.screen.SettingScreen
import com.example.findpill.ui.viewmodel.CalendarViewModel
import com.example.findpill.ui.viewmodel.SettingViewModel

@Composable
fun MainScaffold() {
    val navController = rememberNavController()
    val currentScreen = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold (
        bottomBar = {
            if( currentScreen?.startsWith("detail") == true){
                DetailBottomNavigationBar(navController)
            }
            else if (currentScreen != "loading"){
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = "main", modifier = Modifier.padding(innerPadding)) {
            composable("main") { MainScreen(navController)}
            composable("photosearch") { PhotoSearch(navController) }
            composable("photoing") { Photoing(navController) }
            composable("confirm") { Confirm(navController)}
            composable("loading") { Loading(navController) }
            composable("result") { Result(navController) }
            composable("setting") { val vm: SettingViewModel = viewModel()
                SettingScreen(navController = navController, viewModel = vm) }
            composable("album") { Album(navController) }
            composable("confirm2") { Confirm2(navController) }
            composable("calendar") {
                val vm: CalendarViewModel = viewModel()
                PillCalendar(navController = navController, viewModel = vm) }
            composable("detail/{pillId}") { backStackEntry ->
                val pillId = backStackEntry.arguments?.getString("pillId")?.toIntOrNull()
                pillId?.let{
                    DetailScreen(navController, pillId = it)
                }
            }
        }

    }
}