package com.example.findpill.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.findpill.R
import com.example.findpill.ui.theme.*

@Preview(showBackground = true)
@Composable
fun MainScreen(navController: NavController){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor).padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Row( // 이미지를 End(오른쪽 정렬)로 70퍼센트 만큼 3:1의 비율(aspectRatio)로 배치
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ){
            Image(
                painter = painterResource(id = R.drawable.pill1), contentDescription = null,
                modifier = Modifier.fillMaxWidth(0.7f),
                contentScale = ContentScale.Crop
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ){
            Image(
                painter = painterResource(id = R.drawable.pill2), contentDescription = null,
                modifier = Modifier.fillMaxWidth(0.7f),
                contentScale = ContentScale.Crop
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ){
            Image(
                painter = painterResource(id = R.drawable.pill3), contentDescription = null,
                modifier = Modifier.fillMaxWidth(0.7f),
                contentScale = ContentScale.Crop
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ){
            Image(
                painter = painterResource(id = R.drawable.pill4), contentDescription = null,
                modifier = Modifier.fillMaxWidth(0.7f),
                contentScale = ContentScale.Crop
            )
        }


        Button(
            onClick = { navController.navigate("detail")},
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ){
            Text("시작하기", color = Color.White)
        }
    }
}