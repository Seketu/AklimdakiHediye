package com.reylortechnology.aklimdakihediye.Compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.ObserverClasses.ZodiacStatus


@Composable
fun GiftInfoScreenPrice(
    modifier: Modifier = Modifier,
    label: String,
    stateFirst: MutableState<String>,
    stateSecond: MutableState<String>,
    labelColor: Color
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            label,
            fontSize = 26.sp,
            color = labelColor,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            PriceInformationTextField(
                value1 = stateFirst,
                value2 = stateSecond,
                label1 = "Minimum",
                label2 = "Maximum"
            )
        }
    }
}



@Composable
fun GiftInfoMeaning(
    modifier: Modifier = Modifier,
    buttonModifier: Modifier = Modifier,
    label: String,
    labelColor: Color,
    onClickFirst: () -> Unit,
    onCheckedChangeFirst: (Boolean) -> Unit,
    checkedFirst: Boolean,
    imageFirst: Int,
    buttonTextFirst: String,
    onClickSecond: () -> Unit,
    onCheckedChangeSecond: (Boolean) -> Unit,
    checkedSecond: Boolean,
    imageSecond: Int,
    buttonTextSecond: String,
    ) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            label,
            fontSize = 22.sp,
            textAlign = TextAlign.Start,
            color = labelColor,
        )

        ButtonWithCheckBox(
            modifier = buttonModifier,
            onClick = onClickFirst,
            text = buttonTextFirst,
            checked = checkedFirst,
            onCheckedChange = onCheckedChangeFirst,
            image = imageFirst
        )

        ButtonWithCheckBox(
            modifier = buttonModifier,
            onClick = onClickSecond,
            text = buttonTextSecond,
            checked = checkedSecond,
            onCheckedChange = onCheckedChangeSecond,
            image = imageSecond
        )

    }
}

@Composable
fun InfoScreen(
    modifier: Modifier = Modifier,
    state: MutableState<String>,
    label: String,
    color: Color,
    focusRequester: FocusRequester,
    keyboard: KeyboardOptions
) {
    Column(
        modifier = modifier
            .background(Color.Transparent),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            label,
            fontSize = 22.sp,
            textAlign = TextAlign.Start,
            color = color,
            modifier = Modifier.padding(10.dp)
        )

        Spacer(modifier = Modifier.padding(15.dp))

        UserInfoTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            string = state,
            keyboard = keyboard
        )
    }
}

@Composable
fun ZodiacInfoScreen(
    modifier: Modifier = Modifier,
    zodiacStatus: MutableState<ZodiacStatus>,
    buttonHeigh : Dp = 75.dp
) {

    val zodiacList = remember {
        ZodiacStatus.entries.filter { it != ZodiacStatus.None }
    }
    val chunks = zodiacList.chunked(6)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        chunks.forEach { columnItems ->
            Column(
                modifier = Modifier
                    .weight(1f) // fillMaxWidth(0.5f) yerine weight daha stabildir
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                columnItems.forEach { status ->
                    ZodiacButton(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .height(buttonHeigh),
                        imageSource = status.iconRes,
                        zodiacName = stringResource(status.nameRes),
                        checkBoxState = zodiacStatus,
                        boxZodiac = status
                    )
                }
            }
        }
    }
}

@Composable
fun PersonInfoScreen(
    modifier: Modifier = Modifier,
    state: MutableState<String>,
    label: String,
    color: Color,
    focusRequester: FocusRequester,
    keyboard: KeyboardOptions
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, fontSize = 22.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(25.dp))
        UserInfoTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            string = state,
            keyboard
        )
    }
}
