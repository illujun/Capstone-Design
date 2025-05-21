package com.example.findpill.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.findpill.ui.component.Pill
import com.example.findpill.ui.component.TopBar
import com.example.findpill.ui.component.dummyPillList
import com.example.findpill.ui.utils.getCurrentTime
import com.example.findpill.ui.viewmodel.CalendarViewModel

@Composable
fun PillCalendar(navController: NavController, viewModel: CalendarViewModel){

    val selectedtime = remember { mutableStateOf(getCurrentTime())}

    val morning by viewModel.morning.collectAsState(initial = emptySet())
    val afternoon by viewModel.afternoon.collectAsState(initial = emptySet())
    val night by viewModel.night.collectAsState(initial = emptySet())

    val slots = listOf("morning", "afternoon", "night")



    val curtime = when (selectedtime.value){
        "morning" -> morning
        "afternoon" -> afternoon
        "night" -> night
        else -> emptySet()
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.secondary)
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            TopBar(
                title = "알약 캘린더",
                onBackClick = {navController.popBackStack()}
            )
            Text(
                "다음으로 먹을 알약이에요",
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(vertical = 24.dp)
            )


            BoxWithConstraints {
                val rowWidth = maxWidth * 0.8f
                val paddingWidth = maxWidth * 0.05f
                LazyRow (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(slots) { slot ->
                        Column(
                            modifier = Modifier
                                .width(rowWidth)
                                .background(MaterialTheme.colorScheme.onPrimary, shape= RoundedCornerShape(30.dp))
                                .padding(paddingWidth),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Text(
                                text = when (slot) {
                                    "morning" -> "아침"
                                    "afternoon" -> "점심"
                                    "night" -> "저녁"
                                    else -> ""
                                },
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(20.dp)
                            ){
                                items(dummyPillList) { pill ->
                                    Pill(pill = pill, onClick = {navController.navigate("detail/${pill.id}")})
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}