package com.example.findpill.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun Result(navController: NavController){
    // 여기서도 Loading처럼 유동적으로 json 구조에 맞춰 꺼내 씀
    // val pillId = navController.previousBackStackEntry
    //      ?.savedStateHandle?.get<String>("pill_id")
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.secondary)
    ){

    }
}