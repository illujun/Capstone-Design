package com.example.findpill.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.findpill.R
import com.example.findpill.ui.component.TimeSwitch
import com.example.findpill.ui.component.TopBar
import com.example.findpill.ui.viewmodel.CalendarViewModel

@Composable
fun DetailScreen(navController: NavController, pillId: Int, viewModel: CalendarViewModel) {
    val pillDetail = listOf(
        "약 이름" to "판콜에이",
        "제약회사" to "동아제약",
        "성분" to "아세트아미노펜, 클로르페니라민",
        "색상" to "흰색",
        "제형" to "정제",
        "각인" to "D123",
        "효능/효과" to "감기 증상 완화",
        "용법" to "1일 3회 식후 복용",
        "주의사항" to "졸음 유발 가능, 운전 주의"
    )

    val morning by viewModel.morning.collectAsState(initial = emptySet())
    val afternoon by viewModel.afternoon.collectAsState(initial = emptySet())
    val night by viewModel.night.collectAsState(initial = emptySet())

    val pillIdStr = pillId.toString()

    val morningChecked = remember(morning) { pillIdStr in morning }
    val afternoonChecked = remember(afternoon) { pillIdStr in afternoon }
    val nightChecked = remember(night) { pillIdStr in night }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.secondary)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(title = "알약 정보", onBackClick = { navController.popBackStack() })

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Image(
                        painter = painterResource(R.drawable.pill1),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .wrapContentHeight()
                            .align(Alignment.CenterHorizontally),
                    )
                }

                item{
                    Text(
                        text = "타이레놀",
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 26.sp
                    )
                }

                items(pillDetail) { (title, value) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = title,
                            fontWeight = FontWeight.SemiBold,
                            color = when (title) {
                                "효능/효과", "용법" -> MaterialTheme.colorScheme.primary
                                "주의사항" -> Color.Red
                                else -> MaterialTheme.colorScheme.onSurface
                            },
                            fontSize = 20.sp
                        )
                        Text(
                            text = value,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 20.sp
                        )
                    }
                }
                item{
                    Text(
                        text = "언제 이 알약을 먹나요?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                item{
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        TimeSwitch(title = "아침", time = "morning", isChecked = morningChecked, pillId = pillIdStr, viewModel = viewModel)
                        TimeSwitch(title = "점심", time = "afternoon", isChecked = afternoonChecked, pillId = pillIdStr, viewModel = viewModel)
                        TimeSwitch(title = "저녁", time = "night", isChecked = nightChecked, pillIdStr, viewModel)
                    }
                }
            }
        }
    }
}
