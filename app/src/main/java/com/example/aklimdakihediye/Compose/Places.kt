package com.example.aklimdakihediye.Compose

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.aklimdakihediye.models.ComposeModels.SearchCardModel
import androidx.core.net.toUri


@Composable
fun StepperIndicator(currentStep: Int, modifier: Modifier, totalSteps: Int) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 1..totalSteps) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(if (i == currentStep) 12.dp else 6.dp)
                    .background(
                        if (i == currentStep) Color.DarkGray else Color.Gray,
                        shape = RoundedCornerShape(50)
                    )
            )
            Spacer(Modifier.width(10.dp))
        }
    }
}

@Composable
fun SearchCard(item: SearchCardModel, modifier: Modifier.Companion) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        val context = LocalContext.current
        val safeUrl = if (item.url.startsWith("http")) item.url else "https://${item.url}"
        Row(
            modifier = modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageUrl = remember {
               mutableStateOf(item.imageUrl?.toUri() ?: Uri.EMPTY)
            }
                AsyncImage(
                    model = imageUrl.value,
                    contentDescription = null,
                    modifier = modifier
                        .size(48.dp)
                )


            Column(modifier = modifier.weight(1f)) {
                Text(
                    text = item.name,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.description,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.url,
                    color = Color.Blue,
                    fontSize = 12.sp,
                    modifier = modifier
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, safeUrl.toUri())
                            context.startActivity(intent)
                        }
                )
            }
        }
    }
}
