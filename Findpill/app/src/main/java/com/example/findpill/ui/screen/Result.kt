package com.example.findpill.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.findpill.data.model.PillInfo
import com.example.findpill.ui.component.Pill
import com.example.findpill.ui.component.TopBar
import com.example.findpill.ui.component.dummyPillList
import com.example.findpill.ui.viewmodel.FavoriteViewModel

@Composable
fun Result(navController: NavController){
    var result by remember {mutableStateOf<List<PillInfo>>(emptyList())}
    val pillIds = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<List<Int>>("pill_ids")
    val status = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("status")
    val viewmodel: FavoriteViewModel = hiltViewModel()
    LaunchedEffect(pillIds){
        if(pillIds!=null){
            result = viewmodel.loadPillsByIds(pillIds)
        }
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
            TopBar(title = "${pillIds?.size}개의 검색 결과", onBackClick = {
                navController.popBackStack()
                navController.navigate("photosearch"){

            } })

            if (status != null) {
                val (color, message) = when (status) {
                    "good" -> Color.Green to "알약을 잘 찾아냈습니다."
                    "soso" -> Color.Yellow to "알약 정보가 틀릴 수 있습니다."
                    "bad"  -> Color.Red to "대부분의 알약 정보가 틀릴 수 있습니다."
                    else   -> Color.Gray to "알 수 없는 상태입니다."
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(color, shape = CircleShape)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = message,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                items(whichpill) { pill ->
                    Pill(pill = pill, onClick = {navController.navigate("detail/${pill.pill_id}")})
                }
            }

        }
    }
}