package com.example.findpill.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.findpill.R
import com.example.findpill.data.model.PillInfo
import com.example.findpill.ui.component.TimeSwitch
import com.example.findpill.ui.component.TopBar
import com.example.findpill.ui.component.dummyPillList
import com.example.findpill.ui.viewmodel.CalendarViewModel
import com.example.findpill.ui.viewmodel.GetPillViewModel

@Composable
fun DetailScreen(navController: NavController, pillId: Int, viewModel: CalendarViewModel,
                 viewModel2: GetPillViewModel = hiltViewModel()
) {
    Log.d("GetPillById", "Start DetailScreen, pillId: $pillId")
    LaunchedEffect(pillId){
        Log.d("GetPillById", "Calling fetchPillInfoById with id = $pillId")
        viewModel2.fetchPillInfoById(pillId)
    }

    val pillInfo by viewModel2.pillInfo.collectAsState()

    // null일 경우 예시 데이터로 대체
    val displayData = pillInfo ?: dummyPillList.find{it.id ==pillId } ?: PillInfo(
        image = R.drawable.pill2,
        id = 0,
        name = "타이레놀",
        description = "알약의 특징",
        company = "동아제약",
        material = "아세트아미노펜, 클로르페니라민",
        shape = "모양",
        color = "흰색",
        print_front = "D123",
        print_back = "",
        effect = "감기 증상 완화",
        method = "1일 3회 식후 복용",
        usage = "두통 시에 복용",
        warning = "졸음 유발 가능, 운전 주의",
    )

    val pillDetail = listOf(
        "약 이름" to displayData.name,
        "제약회사" to displayData.company,
        "성분" to displayData.material,
        "색상" to displayData.color,
        "모양" to displayData.shape,
        "각인" to displayData.print_front,
        "효능/효과" to displayData.effect,
        "용법" to displayData.method,
        "주의사항" to displayData.warning
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
                        text = pillDetail[0].second,
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
                        TimeSwitch(title = "저녁", time = "night", isChecked = nightChecked, pillId = pillIdStr, viewModel = viewModel)
                    }
                }
            }
        }
    }
}
