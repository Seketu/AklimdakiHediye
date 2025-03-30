package com.example.aklimdakihediye.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aklimdakihediye.R


@Composable
fun PriceInformationTextField(
    modifier: Modifier = Modifier,
    value1 : MutableState<String>,
    value2 : MutableState<String>,
    label1 : String,
    label2 : String
) {
    Row(
        modifier = modifier
    ) {
        Column (
            Modifier
                .weight(1f)
        ){
            Text(
                label1,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                fontSize = 16.sp
            )
            TextField(
                value1.value,
                onValueChange = {value1.value = it},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                shape = RoundedCornerShape(15.dp),
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Blue,
                ),
            )
        }
        Spacer(Modifier.width(20.dp))
        Column(
            Modifier
                .weight(1f)
        ) {
            Text(label2,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                fontSize = 16.sp
            )
            TextField(
                value2.value,
                onValueChange = {value2.value = it},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(15.dp),
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Blue
                ),
            )
        }
    }
}

@Composable
fun UserInfoTextField(
    modifier: Modifier = Modifier,
    string : MutableState<String>
) {
    Row(
        modifier
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        TextField(
            value = string.value,
            onValueChange = {string.value = it},
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Black,
                disabledContainerColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.White,
            ),
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun InfoTextFields(
    modifier: Modifier = Modifier,
    label: String,
    state: MutableState<String>,
) {
    Row(
        modifier = modifier
    ) {
        TextField(
            value = state.value,
            onValueChange = {state.value = it},
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
            ),
            label = {Text(label)},
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(15.dp)
        )
    }
}