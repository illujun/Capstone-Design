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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.findpill.R
import com.example.findpill.ui.theme.*

@Composable
fun MainScreen(navController: NavController){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ){
        Spacer(modifier = Modifier.height(40.dp))

        Image(painter = painterResource(id = R.drawable.pill1), contentDescription = null)
        Image(painter = painterResource(id = R.drawable.pill2), contentDescription = null)
        Image(painter = painterResource(id = R.drawable.pill3), contentDescription = null)
        Image(painter = painterResource(id = R.drawable.pill4), contentDescription = null)


        Button(
            onClick = { navController.navigate("detail")},
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ){
            Text("시작하기", color = Color.White)
        }
    }
}