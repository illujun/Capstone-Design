package com.example.findpill.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.findpill.data.model.PillInfo
import com.example.findpill.R

@Composable
fun Pill(pill: PillInfo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.onSecondary, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = pill.image,
                    error = painterResource(R.drawable.pill1),
                    placeholder = painterResource(id = R.drawable.pill2)
                ),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Column {
                Text(
                    pill.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    pill.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}


val dummyPillList = listOf(
    PillInfo(
        id = 1234,
        name = "타이레놀 정 500mg",
        description ="공란",
        image = "R.drawable.pill1"
    ),
    PillInfo(
        id = 5678,
        name = "세로나민 캡슐",
        description ="공란",
        image = "R.drawable.pill1"
    ),
    PillInfo(
        id = 9012,
        name = "이부프로펜 정 200mg",
        description ="공란",
        image = "R.drawable.pill1"
    )
)