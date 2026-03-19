package com.reylortechnology.aklimdakihediye.Compose

import DatePickerScreen
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Peoples
import com.reylortechnology.aklimdakihediye.ObserverClasses.ZodiacStatus
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.models.Enums.AgeDescStates
import com.reylortechnology.aklimdakihediye.models.Enums.CharacterTrait
import com.reylortechnology.aklimdakihediye.models.Enums.Genders
import com.reylortechnology.aklimdakihediye.models.Enums.Hobbies
import com.reylortechnology.aklimdakihediye.models.Enums.ProfileColors
import com.reylortechnology.aklimdakihediye.models.Enums.TypeRelationship
import com.reylortechnology.aklimdakihediye.ui.theme.onSaveDialogBg
import com.reylortechnology.aklimdakihediye.ui.theme.saveDialogBg
import java.time.LocalDate
import java.time.Period


@SuppressLint("LocalContextGetResourceValueCall")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddPeopleDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onSaveClick: (Peoples) -> Unit
) {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current

    val heightPx = windowInfo.containerSize.height
    val screenHeight = with(density) { heightPx.toDp() }

    val profileImages = listOf(
        R.drawable.man_1,
        R.drawable.man_2,
        R.drawable.woman_2,
        R.drawable.mom_1,
        R.drawable.mom_2
    )

    val peopleName = remember {
        mutableStateOf("")
    }
    val peopleJob = remember {
        mutableStateOf("")
    }

    val peopleBestSide = remember {
        mutableStateOf("")
    }

    val selectedZodiac = remember {
        mutableStateOf(ZodiacStatus.None)
    }

    val selectedCharacterTrait = remember {
        mutableStateListOf<CharacterTrait>()
    }

    val sortedCharacterTrait = remember(selectedCharacterTrait.toList()) {
        CharacterTrait.entries.sortedByDescending { characterTrait ->
            selectedCharacterTrait.contains(
                characterTrait
            )
        }
    }

    val selectedHobbies = remember {
        mutableStateListOf<Hobbies>()
    }

    val sortedHobbies = remember(selectedHobbies.toList()) {
        Hobbies.entries.sortedByDescending { selectedHobbies.contains(it) }
    }
    val context = LocalContext.current
    val today = remember { LocalDate.now() }
    var selectedDay by remember { mutableIntStateOf(1) }
    var selectedMonth by remember { mutableIntStateOf(1) }
    var selectedYear by remember { mutableIntStateOf(today.year) }


    val calculatedAge = remember(selectedDay, selectedMonth, selectedYear) {
        val today = LocalDate.now()

        try {
            val birthdate = LocalDate.of(selectedYear, selectedMonth, selectedDay)
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
    val selectedRelationships = remember {
        mutableStateOf<TypeRelationship?>(
            null
        )
    }
    val selectedColor = remember { mutableStateOf(ProfileColors.Brown.color) }

    var selectedImageId: Int by remember { mutableIntStateOf(1) }

    val selectedGender = remember {
        mutableStateOf<Genders?>(null)
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = modifier
                .background(
                    saveDialogBg,
                    shape = MaterialTheme.shapes.medium
                )
        ) {
            //Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding(),
                contentPadding = PaddingValues(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                item {

                    Text(
                        "Fotoğraf Seç",
                        style = MaterialTheme.typography.labelMedium,
                        color = onSaveDialogBg
                    )


                    //Photo Field

                    PeoplePhotoSelector(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .height(screenHeight * 0.12f),
                        photoList = profileImages,
                        photoColor = selectedColor.value,
                        onColorAndImageSelected = { imageId, color ->
                            selectedColor.value = color
                            selectedImageId = imageId
                        },
                        cardSize = 100.dp
                    )


                    //Color Field
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Renk Seçin",
                            style = MaterialTheme.typography.bodyLarge,
                            color = onSaveDialogBg
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
                                        .fillMaxHeight(0.70f)
                                        .aspectRatio(1f),
                                    isSelected = selectedColor.value == it.color
                                ) { selected ->
                                    selectedColor.value = selected
                                }
                            }
                        }
                    }
                }

                item {
                    //People Name Field
                    BasicTextField(
                        value = peopleName.value,
                        onValueChange = { peopleName.value = it },
                        decorationBox = { innerField ->
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(shape = MaterialTheme.shapes.medium)
                                    .background(
                                        MaterialTheme.colorScheme.secondary
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(0.2f)
                                        .background(MaterialTheme.colorScheme.primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "İsim",
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(10.dp)
                                ) {
                                    if (peopleName.value.isNullOrEmpty()) {
                                        Text(
                                            text = "Lüften Arkadaşınızın ismini yazın...",
                                            color = MaterialTheme.colorScheme.onSecondary
                                        )
                                    } else {
                                        innerField()
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .height(screenHeight * 0.06f)
                            .fillMaxWidth(0.89f),
                    )
                }
                //PeopleJob Field
                item{
                    BasicTextField(
                        value = peopleJob.value,
                        onValueChange = { peopleJob.value = it },
                        decorationBox = { innerField ->
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(shape = MaterialTheme.shapes.medium)
                                    .background(
                                        MaterialTheme.colorScheme.secondary
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(0.2f)
                                        .background(MaterialTheme.colorScheme.primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "Meslek",
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(10.dp)
                                ) {
                                    if (peopleJob.value.isNullOrEmpty()) {
                                        Text(
                                            text = "Lüften Mesleğini Yazınız...",
                                            color = MaterialTheme.colorScheme.onSecondary
                                        )
                                    } else {
                                        innerField()
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .height(screenHeight * 0.06f)
                            .fillMaxWidth(0.89f),
                    )
                }

                item {
                    //People BestSize Fİeld
                    BasicTextField(
                        value = peopleBestSide.value,
                        onValueChange = { peopleBestSide.value = it },
                        decorationBox = { innerField ->
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(shape = MaterialTheme.shapes.medium)
                                    .background(
                                        MaterialTheme.colorScheme.secondary
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(0.2f)
                                        .background(MaterialTheme.colorScheme.primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "İyi Yönler",
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        textAlign = TextAlign.Center
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(10.dp)
                                ) {
                                    if (peopleBestSide.value.isNullOrEmpty()) {
                                        Text(
                                            text = "Lüften İyi Yönlerini Yazınız...",
                                            color = MaterialTheme.colorScheme.onSecondary
                                        )
                                    } else {
                                        innerField()
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .height(screenHeight * 0.06f)
                            .fillMaxWidth(0.89f),
                    )
                }

                item {
                    //ReleationShips Fields

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .heightIn(min = screenHeight * 0.01f),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.95f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(25.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "1",
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            Text(
                                text = "Bu Kaydettiğimiz Kişi Kim ?",
                                color = onSaveDialogBg,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(5.dp), // Yatay boşluk
                            verticalArrangement = Arrangement.spacedBy(1.dp)   // Dikey boşluk (alt satıra geçince)
                        ) {
                            TypeRelationship.entries.forEach { relationship ->
                                val isSelected = selectedRelationships.value == relationship
                                ColoredCheckBox(
                                    modifier = Modifier,
                                    text = stringResource(relationship.stringRes),
                                    selectedValue = isSelected,
                                ) {
                                    if (selectedRelationships.value == relationship) {
                                        selectedRelationships.value = null
                                    } else {
                                        selectedRelationships.value = relationship
                                    }
                                }
                            }
                        }
                    }


                    //Genders Fields
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .heightIn(min = screenHeight * 0.01f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.95f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(25.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "2",
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            Text(
                                text = "Cinsiyeti Seçin",
                                color = onSaveDialogBg,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth(0.97f),
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Genders.entries.forEach { genders ->
                                ColoredCheckBox(
                                    modifier = Modifier,
                                    text = stringResource(genders.titleId),
                                    selectedValue = selectedGender.value == genders
                                ) {
                                    if (selectedGender.value == genders) {
                                        selectedGender.value = null
                                    } else {
                                        selectedGender.value = genders
                                    }
                                }
                            }
                        }
                    }


                    //BirthDay

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

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.95f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.95f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(25.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "3",
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            Text(
                                text = "Doğum Tarihi",
                                color = onSaveDialogBg,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Box(
                                modifier = Modifier
                                    .height(25.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = MaterialTheme.shapes.medium
                                    )
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = calculatedAge,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .height(screenHeight * 0.15f)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            DatePickerScreen(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(0.65f),
                                onDateChange = { d, m, y ->
                                    selectedDay = d
                                    selectedMonth = m
                                    selectedYear = y
                                }
                            )
                            AgeDescSurface(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(0.9f),
                                ageState = descAgedState.value
                            )
                        }
                    }

                }

                item {

                    //Hobbies
                    Column(
                        modifier = Modifier
                            .heightIn(min = screenHeight * 0.3f)
                            .fillMaxWidth(0.97f),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.95f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(25.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "4",
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            Text(
                                text = "Hobileri Nelerdir ?",
                                color = onSaveDialogBg,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            sortedHobbies.forEach { hobby ->
                                val isSelected = selectedHobbies.contains(hobby)
                                ColoredCheckBox(
                                    modifier = Modifier,
                                    text = stringResource(hobby.textSource),
                                    selectedValue = isSelected,
                                    prefixImage = hobby.iconSource
                                ) {
                                    if (isSelected) {
                                        selectedHobbies.remove(hobby)
                                    } else {
                                        selectedHobbies.add(hobby)
                                    }
                                }
                            }
                        }
                    }


                    //Character
                    Column(
                        modifier = Modifier
                            .heightIn(max = screenHeight * 0.3f)
                            .fillMaxWidth(0.97f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.95f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(25.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "5",
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            Text(
                                text = "Karakteristik özellikleri ?",
                                color = onSaveDialogBg,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        FlowRow(
                            modifier = Modifier,
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            sortedCharacterTrait.forEach { trait ->
                                val isSelected = selectedCharacterTrait.contains(trait)
                                ColoredCheckBox(
                                    modifier = Modifier,
                                    text = stringResource(trait.textSource),
                                    selectedValue = isSelected
                                ) {
                                    if (isSelected) {
                                        selectedCharacterTrait.remove(trait)
                                    } else {
                                        selectedCharacterTrait.add(trait)
                                    }
                                }
                            }
                        }
                    }


                    //Zodiac
                    Column(
                        modifier = Modifier
                            .heightIn(max = screenHeight * 0.3f)
                            .fillMaxWidth(0.97f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.95f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(25.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "6",
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            Text(
                                text = "Burcu Nedir ?",
                                color = onSaveDialogBg,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        FlowRow(
                            modifier = Modifier,
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            ZodiacStatus.entries.filter { it != ZodiacStatus.None }
                                .forEach { zodiac ->
                                    val isSelected = selectedZodiac.value == zodiac
                                    ColoredCheckBox(
                                        modifier = Modifier,
                                        text = stringResource(zodiac.nameRes),
                                        selectedValue = isSelected,
                                        prefixImage = zodiac.iconRes
                                    ) {
                                        selectedZodiac.value = zodiac
                                    }
                                }
                        }
                    }
                }

                item {

                    //Button
                    FilledTonalButton(
                        onClick = {
                            if (peopleName.value.isNotEmpty() &&
                                peopleJob.value.isNotEmpty() &&
                                peopleBestSide.value.isNotEmpty() &&
                                selectedRelationships.value != null &&
                                selectedGender.value != null &&
                                selectedZodiac.value != ZodiacStatus.None &&
                                selectedCharacterTrait.isNotEmpty() &&
                                selectedHobbies.isNotEmpty()
                                ) {
                                onSaveClick(
                                    Peoples(
                                        peopleName = peopleName.value,
                                        birthday = LocalDate.of(selectedYear, selectedMonth, selectedDay) ,
                                        age = calculatedAge.toInt(),
                                        zodiac = selectedZodiac.value.name,
                                        hobbies = selectedHobbies.joinToString(","){it.name},
                                        bestSide = peopleBestSide.value,
                                        character = selectedCharacterTrait.joinToString(",") { it.name },
                                        job = peopleJob.value,
                                        image = selectedImageId,
                                        color = selectedColor.value,
                                        relationship = selectedRelationships.value!!
                                    )
                                )
                            } else {
                                Toast
                                    .makeText(
                                        context,
                                        "Lütfen Gerekli Alanları Doldurun",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        },
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.fillMaxWidth(0.95f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        border = BorderStroke(5.dp, MaterialTheme.colorScheme.onPrimary)
                    ) {

                        Image(
                            painter = painterResource(R.drawable.save_flag_icon),
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                            modifier = Modifier
                                .fillMaxWidth(0.1f)
                                .aspectRatio(1f)
                        )

                        Text(
                            text = stringResource(R.string.save_text),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
            //CloseIcon
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(30.dp)
                    .background(
                        MaterialTheme.colorScheme.error,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(5.dp)
                    .clickable(
                        onClick = {
                            onDismissRequest()
                        }
                    )
            ) {
                Icon(
                    painter = painterResource(R.drawable.close_icon),
                    contentDescription = "Close save",
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        }
    }
}

@Composable
fun AlertDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    confirmButton: () -> Unit,
    dismissButton: () -> Unit,
    title: String,
    confirmText: String,
    dismissText: String,
    withDismiss : Boolean = true
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            Button(
                onClick = confirmButton,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(confirmText, color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        dismissButton = if (withDismiss) {
            @Composable{
                Button(
                    onClick = dismissButton,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(dismissText, color = MaterialTheme.colorScheme.onError)
                }
            }
        } else null,
        title = {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun PopPreview() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AddPeopleDialog(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.9f),
            onDismissRequest = {},
            onSaveClick = {}
        )
    }
}