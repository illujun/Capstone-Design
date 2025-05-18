package com.example.findpill.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

            val slots = listOf("morning", "afternoon", "night")
            LazyRow (
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                items(slots) { slot ->
                    Button(
                        onClick = { selectedtime.value = slot },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedtime.value == slot) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ){
                        Text(slot)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                items(curtime.toList()) { pillId ->
                    val pill = dummyPillList.find { it.id == pillId.toInt() }

                    pill?.let { Pill(pill = it) }
                }
            }
        }
    }
}