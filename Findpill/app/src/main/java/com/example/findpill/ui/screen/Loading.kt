package com.example.findpill.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.findpill.data.repository.UploadImage
import kotlinx.coroutines.delay

@Composable
fun Loading(navController: NavController){
    val pill1 = navController.previousBackStackEntry?.savedStateHandle?.get<String>("temp_pill1")
    val pill2 = navController.previousBackStackEntry?.savedStateHandle?.get<String>("temp_pill2")
    val context = LocalContext.current
    val uploadR = remember { UploadImage(context) }

    var showSuccess by remember { mutableStateOf(false)}
    var showFailure by remember { mutableStateOf(false)}

    LaunchedEffect(Unit){
        if(pill1 != null && pill2 != null){
            try{
                val result = uploadR.upload(pill1, pill2)
                delay(500L)
                if(result==true){
                    showSuccess = true

                    navController.currentBackStackEntry?.savedStateHandle?.apply{
                        // 이 부분은 json body 구성 요소에 따라 유동적으로 조정
                        // set("pill_id", result.pill_id) 등
                    }
                    navController.navigate("result")
                }else{
                    showFailure = true
                    Log.d("UploadImage", "response가 NULL임")
                }
            }catch(e:Exception){
                showFailure = true
                Log.d("UploadImage", "try 블럭 오류 발생")
            }
        }else{
            showFailure = true
            Log.d("UploadImage", "이미지가 널 값임")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.secondary),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "알약 정보를 분석 중입니다...",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
                )
        }

        if(showSuccess){
            AlertDialog(
                onDismissRequest= {},
                title = { Text("성공")},
                text = { Text("사진 전송이 완료되었습니다.")},
                confirmButton = {
                    Button(onClick = {
                        showSuccess = false
                    }){
                        Text("확인")
                    }
                }

            )
        }

        if(showFailure){
            AlertDialog(
                onDismissRequest = {},
                title = { Text("실패")},
                text = {Text("사진 업로드에 실패했습니다. 다시 시도해 주세요.")},
                confirmButton = {
                    Button(onClick = {
                        showFailure = false
                        navController.previousBackStackEntry?.savedStateHandle?.apply {
                            set("temp_pill1", pill1)
                            set("temp_pill2", pill2)
                        }
                        navController.popBackStack()
                    }){
                        Text("확인")
                    }
                }
            )
        }
    }
}