package com.example.findpill.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.findpill.data.model.PillInfo

@Composable
fun Pill(pill: PillInfo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onSecondary, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(pill.image),
            contentDescription = null,
            modifier = Modifier.size(64.dp).padding(end = 12.dp)
        )
        Column{
            Text(pill.name, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
            Text(pill.description, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(top = 12.dp))
        }
    }
}

@Composable
fun PillTestList() {
    Column {
        dummyPillList.forEach {
            Pill(pill = it)
        }
    }
}

val dummyPillList = listOf(
    PillInfo(
        id = 1234,
        name = "타이레놀 정 500mg",
        description ="공란",
        image = "https://cdn-icons-png.flaticon.com/512/809/809957.png"
    ),
    PillInfo(
        id = 5678,
        name = "세로나민 캡슐",
        description ="공란",
        image = "https://cdn-icons-png.flaticon.com/512/2921/2921822.png"
    ),
    PillInfo(
        id = 9012,
        name = "이부프로펜 정 200mg",
        description ="공란",
        image = "https://cdn-icons-png.flaticon.com/512/2921/2921820.png"
    )
)