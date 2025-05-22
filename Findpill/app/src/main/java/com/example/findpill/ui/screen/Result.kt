package com.example.findpill.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.findpill.data.model.PillInfo
import com.example.findpill.ui.component.Pill
import com.example.findpill.ui.component.TopBar
import com.example.findpill.ui.component.dummyPillList

@Composable
fun Result(navController: NavController){
    // 여기서도 Loading처럼 유동적으로 json 구조에 맞춰 꺼내 씀
    // val pillId = navController.previousBackStackEntry
    //      ?.savedStateHandle?.get<String>("pill_id")
    val result = remember {
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<List<PillInfo>>("pill_list")
    }

    val whichpill = if(result.isNullOrEmpty()) dummyPillList else result

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.secondary)
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            TopBar(title = "검색 결과", onBackClick = {
                navController.popBackStack()
                navController.navigate("photosearch"){

            } })

              LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                items(whichpill) { pill ->
                    Pill(pill = pill, onClick = {navController.navigate("detail/${pill.id}")})
                }
            }

        }
    }
}