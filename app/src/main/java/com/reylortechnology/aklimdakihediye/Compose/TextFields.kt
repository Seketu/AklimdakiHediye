package com.reylortechnology.aklimdakihediye.Compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reylortechnology.aklimdakihediye.ui.theme.secondaryContainerLight


@Composable
fun SpecialDayTextField(
    modifier: Modifier = Modifier,
    value : MutableState<String>,
    label : String,
    isError : MutableState<Boolean>
) {
    TextField(
        modifier = modifier,
        value = value.value,
        onValueChange = {value.value = it},
        label = {
            Text(label)
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            focusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
            unfocusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
            focusedIndicatorColor = MaterialTheme.colorScheme.onSecondaryContainer,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSecondaryContainer,
            focusedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        shape = RoundedCornerShape(
            topStart = 5.dp,
            topEnd = 5.dp,
        ),
        isError = isError.value,
        prefix = {
            Icon(
                Icons.Default.AccountCircle,
                ""
            )
        }
    )
}

@Composable
fun UserInformationRow(
    modifier: Modifier = Modifier,
    label : String,
    value : MutableState<String>,
    enabled : MutableState<Boolean>
) {
    val localConfiguration = LocalConfiguration.current
    val screenWidth = localConfiguration.screenWidthDp.dp
    Row(
        modifier = modifier
            .background(
                color = secondaryContainerLight,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(label)
        Spacer(Modifier.weight(1f))
        TextField(
            value.value,
            onValueChange = {value.value = it},
            shape = RoundedCornerShape(0.dp),
            enabled = enabled.value,
            modifier = Modifier.width(screenWidth * 0.4f),
            colors = TextFieldDefaults.colors()
        )
    }
}

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
fun UserSettingsTextField(
    modifier: Modifier = Modifier,
    value : MutableState<String>,
    prefix : String,
    keyboard: KeyboardOptions = KeyboardOptions.Default
) {
    TextField(
        value = value.value,
        onValueChange = {value.value = it},
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.secondary,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
            focusedTextColor = MaterialTheme.colorScheme.onSecondary,
            unfocusedTextColor = MaterialTheme.colorScheme.onSecondary
        ),
        shape = MaterialTheme.shapes.extraSmall,
        modifier = modifier,
        prefix = {
            Text(
                text = prefix,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.labelMedium
                )
        },
        textStyle = MaterialTheme.typography.titleLarge,
        keyboardOptions = keyboard
    )
}

@Composable
fun UserInfoTextField(
    modifier: Modifier = Modifier,
    string: MutableState<String>,
    keyboard: KeyboardOptions
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
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = keyboard
        )
    }
}
