package com.example.aklimdakihediye.Compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


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
fun SearchCard(
    modifier: Modifier = Modifier,
    urlName: String,
    description: String
) {
    Row(
        modifier = modifier
    ) {

    }
}