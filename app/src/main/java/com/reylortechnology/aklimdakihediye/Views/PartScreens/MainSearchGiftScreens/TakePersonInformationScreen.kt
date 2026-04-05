package com.reylortechnology.aklimdakihediye.Views.PartScreens.MainSearchGiftScreens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reylortechnology.aklimdakihediye.Compose.AgeDescSurface
import com.reylortechnology.aklimdakihediye.Compose.ColorButton
import com.reylortechnology.aklimdakihediye.Compose.ColoredCheckBox
import com.reylortechnology.aklimdakihediye.Compose.DatePickerScreen
import com.reylortechnology.aklimdakihediye.Compose.PeoplePhotoSelector
import com.reylortechnology.aklimdakihediye.Compose.clearIndicatorColors
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.models.Enums.AgeDescStates
import com.reylortechnology.aklimdakihediye.models.Enums.Genders
import com.reylortechnology.aklimdakihediye.models.Enums.PeopleImages
import com.reylortechnology.aklimdakihediye.models.Enums.ProfileColors
import com.reylortechnology.aklimdakihediye.models.Enums.TypeRelationship
import com.reylortechnology.aklimdakihediye.ui.theme.poppinsFontFamily
import java.time.LocalDate
import java.time.Period

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TakePersonInformationScreen(
    modifier: Modifier = Modifier,
    peopleName: MutableState<String>,
    typeOfRelationship: MutableState<TypeRelationship?>,
    selectedGender: MutableState<Genders?>,
    selectedDay: MutableIntState,
    selectedMonth: MutableIntState,
    selectedYear: MutableIntState,
    selectedColor : MutableState<Color>,
    selectedImageId : MutableState<Int>,
    onNext: (Int) -> Unit
) {

    val screenConf = LocalConfiguration.current
    val screenHeight  = screenConf.screenHeightDp.dp
    val calculatedAge = remember(selectedDay.intValue, selectedMonth.intValue, selectedYear.intValue) {
        val today = LocalDate.now()

        try {
            val birthdate = LocalDate.of(selectedYear.intValue, selectedMonth.intValue, selectedDay.intValue)
            if (birthdate.isAfter(today)) {
                "0"
            } else {
                Period.between(birthdate, today).years.toString()
            }
        } catch (e: Exception) {
            Log.e("Error at take Age", e.message.toString())
            "0"
        }
    }
    val descAgedState = remember(calculatedAge) {
        val ageInt = calculatedAge.toInt()
        derivedStateOf {
            when (ageInt) {
                in 0..14 -> {
                    AgeDescStates.Child
                }

                in 15..20 -> {
                    AgeDescStates.Teen
                }

                in 21..35 -> {
                    AgeDescStates.Adult
                }

                else -> {
                    AgeDescStates.Older
                }
            }
        }
    }
    Box(
        modifier = modifier
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Column{
                Text(
                    text = buildAnnotatedString {
                        withStyle(style =
                            SpanStyle(
                                fontFamily = poppinsFontFamily,
                                fontSize = 35.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface,
                                letterSpacing = (-2).sp,
                            )
                        ){
                            append(stringResource(
                                R.string.screen_title_take_person_information_black_main_search_gift
                            ))
                        }
                        append(" ")
                        withStyle(
                            style = SpanStyle(
                                fontFamily = poppinsFontFamily,
                                fontSize = 35.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Red
                            )
                        ){
                            append(stringResource(R.string.screen_title_take_person_information_red_main_search_gift))
                        }
                    },
                )
                Text(
                    text = "Başlarken bazı bilgileri anlat :)",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontFamily = poppinsFontFamily,
                    ),
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.8f
                    )
                )
            }


            PeoplePhotoSelector(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(screenHeight * 0.12f),
                photoList = PeopleImages.entries.map { it.imageRes }.toList(),
                photoColor = selectedColor.value,
                onColorAndImageSelected = { imageId, color ->
                    selectedColor.value = color
                    selectedImageId.value = imageId
                },
                cardSize = 100.dp
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Renk Seçin",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .height(screenHeight * 0.05f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ProfileColors.entries.forEach {
                        ColorButton(
                            color = it,
                            modifier = Modifier
                                .height((screenHeight * 0.05f) * 0.70f)
                                .aspectRatio(1f),
                            isSelected = selectedColor.value == it.color
                        ) { selected ->
                            selectedColor.value = selected
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
                TextField(
                    value = peopleName.value,
                    onValueChange = { peopleName.value = it },
                    colors = TextFieldDefaults.clearIndicatorColors(),
                    maxLines = 2,
                    label = {
                        Text(
                            text = stringResource(R.string.label_textfield_person_name)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    supportingText = {
                        Text(
                            "Bu kişinin adı ne ?"
                        )
                    },
                    shape = MaterialTheme.shapes.small
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        "1",
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape
                            )
                            .padding(10.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Text(
                        "Bu hediyeyi kimin için arıyoruz ?",
                        fontFamily = poppinsFontFamily,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TypeRelationship.entries.forEach { relationship ->
                        ColoredCheckBox(
                            text = stringResource(relationship.stringRes),
                            selectedValue = typeOfRelationship.value == relationship,
                        ) {
                            typeOfRelationship.value = relationship
                        }
                    }
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        "2",
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape
                            )
                            .padding(10.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Text(
                        "Cinsiyeti nedir ?",
                        fontFamily = poppinsFontFamily,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Genders.entries.forEach {genders ->
                        ColoredCheckBox(
                            text = stringResource(genders.stringRes),
                            selectedValue = selectedGender.value == genders,
                        ) {
                            selectedGender.value = genders
                        }
                    }
                }
            }
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        "3",
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape
                            )
                            .padding(10.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Text(
                        "Doğum Tarihi nedir ?",
                        fontFamily = poppinsFontFamily,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight * 0.12f),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DatePickerScreen(
                        modifier = Modifier
                            .fillMaxWidth(0.5f),
                        initialDay = selectedDay.intValue,
                        initialMonth = selectedMonth.intValue,
                        initialYear = selectedYear.intValue,
                    ) {day, month, year ->
                        selectedDay.intValue = day
                        selectedMonth.intValue = month
                        selectedYear.intValue = year
                    }

                    AgeDescSurface(
                        modifier = Modifier,
                        ageState = descAgedState.value
                    )
                }
            }
            Spacer(Modifier.height(screenHeight * 0.075f))
            AnimatedContent(
                targetState =
                            typeOfRelationship.value != null &&
                            selectedGender.value != null &&
                            peopleName.value.isNotBlank() &&
                            calculatedAge.toInt() >= 5 &&
                            selectedColor.value != Color.Transparent,
                label = "Next Button Animation"
            ){
                if(it){
                    Button(
                        onClick = { onNext(calculatedAge.toInt()) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.15f),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Text(
                            stringResource(R.string.label_next_step),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun TakePersonPrev() {
    val today = LocalDate.now()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.surface
            )
    ){
        TakePersonInformationScreen(
            onNext = {},
            modifier = Modifier.fillMaxSize(),
            peopleName = remember { mutableStateOf("")},
            typeOfRelationship = remember { mutableStateOf<TypeRelationship?>(null) },
            selectedGender = remember { mutableStateOf<Genders?>(null) },
            selectedDay = remember { mutableIntStateOf(1) },
            selectedMonth = remember { mutableIntStateOf(1) },
            selectedYear = remember { mutableIntStateOf(today.year) },
            selectedImageId = remember { mutableStateOf(0) },
            selectedColor = remember { mutableStateOf(Color.Transparent) }
        )
    }

}