package com.example.findpill.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.findpill.R
import com.example.findpill.ui.component.TopBar

@Composable
fun DetailScreen(navController: NavController, pillId: Int) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.secondary)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(title = "알약 정보", onBackClick = { navController.popBackStack() })

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly

            ){
                Image(
                    painter = rememberAsyncImagePainter(
                        model = R.drawable.pill1 // 수정 요망
                    ),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                Text(
                    text = "약 이름",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp
                )
                Text(
                    text = "약에 대한 설명 약에 대한 설명 약에 대한 설명 약에 대한 설명 약에 대한 설명"
                )
            }
        }
    }
}
