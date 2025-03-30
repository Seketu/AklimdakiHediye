package com.example.aklimdakihediye.compose

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aklimdakihediye.R


@Composable
fun StepperIndicator(currentStep: Int,modifier: Modifier,totalSteps : Int) {
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
fun DailyUserInfoFieldsHolder(
    modifier: Modifier = Modifier,
    context: Context,
    state1 : MutableState<String>,
    state2 : MutableState<String>,
    state3 : MutableState<String>,
    label1 :String,
    label2 :String,
    label3 :String,
    containerLabel : String
) {
    Column(
        modifier = modifier
            .fillMaxWidth(0.8f)
            .height(IntrinsicSize.Max),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = containerLabel,
            color = Color.White,
            fontSize = 14.sp
        )

        Column(
            Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(Modifier.height(5.dp))

            UserInfoTextField(
                modifier = Modifier,
                state1
            )
            Spacer(Modifier.height(5.dp))
            UserInfoTextField(
                modifier = Modifier,
                state2
            )
            Spacer(Modifier.height(5.dp))
            UserInfoTextField(
                modifier = Modifier,
                state3
            )
            Spacer(Modifier.height(5.dp))
        }
    }
}

@Composable
fun DailyInfoPlace(
    modifier: Modifier = Modifier,
    labels : List<String>,
    state : List<MutableState<String>>
) {
    Column(
        modifier = modifier
            .height(IntrinsicSize.Max),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        for((index,label) in labels.withIndex()){
            Spacer(Modifier.height(15.dp))
            InfoTextFields(
                modifier = Modifier
                    .fillMaxWidth(0.85f),
                label = label,
                state = state.get(index),
            )
            Spacer(Modifier.height(5.dp))
        }
        Spacer(Modifier.height(15.dp))
    }
}
