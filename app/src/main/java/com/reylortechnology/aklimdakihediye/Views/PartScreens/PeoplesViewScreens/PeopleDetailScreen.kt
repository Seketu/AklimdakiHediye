package com.reylortechnology.aklimdakihediye.Views.PartScreens.PeoplesViewScreens

import android.content.Context
import com.reylortechnology.aklimdakihediye.Compose.DatePickerScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.reylortechnology.aklimdakihediye.Compose.AgeDescSurface
import com.reylortechnology.aklimdakihediye.Compose.ColoredCheckBox
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Peoples
import com.reylortechnology.aklimdakihediye.ObserverClasses.ZodiacStatus
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.models.ComposeModels.PeopleDetailsDividerState
import com.reylortechnology.aklimdakihediye.models.Enums.AgeDescStates
import com.reylortechnology.aklimdakihediye.models.Enums.CharacterTrait
import com.reylortechnology.aklimdakihediye.models.Enums.Genders
import com.reylortechnology.aklimdakihediye.models.Enums.Hobbies
import com.reylortechnology.aklimdakihediye.models.Enums.TypeRelationship
import java.time.LocalDate
import java.time.Period

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PeopleDetailScreen(
    modifier: Modifier = Modifier,
    context: Context,
    peoples: MutableState<Peoples>,
    selectedPeople: Peoples? = null,
    openImageDialog: (Peoples) -> Unit,
    updatePeopleInformation: (Peoples)-> Unit = { _ -> }
) {
    val screenConf = LocalConfiguration.current
    val screenHeight = screenConf.screenHeightDp.dp

    // Use selectedPeople if available, otherwise fall back to peoples.value
    val currentPeople = selectedPeople ?: peoples.value
    var peopleInformation by remember(currentPeople) { mutableStateOf(currentPeople) }

    // Update peopleInformation when selectedPeople changes (e.g., from image selection)
    LaunchedEffect(selectedPeople) {
        if (selectedPeople != null) {
            peopleInformation = selectedPeople
        }
    }

    val isInformationChange = remember(peopleInformation) {
        derivedStateOf {
            peopleInformation != peoples.value
        }
    }
    val pagerState = rememberPagerState(pageCount = { PeopleDetailsDividerState.entries.size }, initialPage = 0)
    val paddingSave = remember(isInformationChange.value) {
        if (isInformationChange.value) screenHeight * 0.08f else 0.dp
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    bottom = paddingSave
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image Section
            Box(
                modifier = Modifier
                    .clickable {
                        openImageDialog(peopleInformation)
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .clip(CircleShape)
                        .height(screenHeight * 0.2f)
                        .aspectRatio(1f)
                        .drawWithContent {
                            drawContent()
                            drawCircle(
                                Brush.radialGradient(
                                    0.84f to Color.Transparent,
                                    1f to peopleInformation.color
                                ),
                            )
                        },
                    painter = painterResource(peopleInformation.image),
                    contentDescription = stringResource(R.string.content_description_user_profile),
                    contentScale = ContentScale.FillHeight
                )

                Icon(
                    Icons.Default.Edit,
                    stringResource(R.string.content_description_edit_profile_image),
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.inverseSurface
                        )
                        .padding(10.dp)
                        .align(Alignment.BottomEnd),
                    tint = MaterialTheme.colorScheme.inverseOnSurface
                )
            }

            // Page Indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PeopleDetailsDividerState.entries.forEachIndexed { index, _ ->
                    val isSelected = pagerState.currentPage == index
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(PeopleDetailsDividerState.entries[index].stringRes),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(
                                    width = if (isSelected) 24.dp else 8.dp,
                                    height = 8.dp
                                )
                                .background(
                                    color = if (isSelected)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }

            // HorizontalPager with different sections
            HorizontalPager(
                modifier = Modifier
                    .weight(1f),
                state = pagerState
            ) { page ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(vertical = 20.dp)
                ) {
                    when (PeopleDetailsDividerState.entries[page]) {
                        // Page 0: Personal and Relationship Information
                        PeopleDetailsDividerState.PersonalAndRelationInformation -> {
                            // Personal Information Section
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .align(Alignment.Start),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.People,
                                            contentDescription = "",
                                            modifier = Modifier
                                                .background(
                                                    color = MaterialTheme.colorScheme.primary,
                                                    shape = CircleShape
                                                )
                                                .padding(5.dp),
                                            tint = MaterialTheme.colorScheme.onPrimary
                                        )

                                        Text(
                                            text = stringResource(R.string.label_personal_information),
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    TextField(
                                        value = peopleInformation.peopleName,
                                        onValueChange = { peopleInformation = peopleInformation.copy(peopleName = it) },
                                        colors = TextFieldDefaults.colors(
                                            disabledIndicatorColor = Color.Transparent,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        shape = MaterialTheme.shapes.small
                                    )

                                    TextField(
                                        value = peopleInformation.job,
                                        onValueChange = { peopleInformation = peopleInformation.copy(job = it) },
                                        colors = TextFieldDefaults.colors(
                                            disabledIndicatorColor = Color.Transparent,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            focusedContainerColor = Color.Transparent,
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        shape = MaterialTheme.shapes.small,
                                    )

                                    FlowRow(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Genders.entries.forEach { genders ->
                                            ColoredCheckBox(
                                                modifier = Modifier,
                                                text = stringResource(genders.stringRes),
                                                selectedValue = peopleInformation.gender == genders
                                            ) {
                                                peopleInformation = peopleInformation.copy(gender = genders)
                                            }
                                        }
                                    }

                                    Text(
                                        text = stringResource(R.string.label_old) + ": ${peopleInformation.age}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.align(Alignment.Start)
                                    )

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(screenHeight * 0.12f),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                                    ) {
                                        DatePickerScreen(
                                            modifier = Modifier
                                                .fillMaxWidth(0.5f),
                                            initialDay = peopleInformation.birthday.dayOfMonth,
                                            initialMonth = peopleInformation.birthday.monthValue,
                                            initialYear = peopleInformation.birthday.year
                                        ) { day, month, year ->
                                            val newDate = LocalDate.of(year, month, day)
                                            peopleInformation = peopleInformation.copy(birthday = newDate)
                                            peopleInformation = peopleInformation.copy(age = Period.between(newDate, LocalDate.now()).years)
                                        }

                                        AgeDescSurface(
                                            modifier = Modifier,
                                            ageState = when (peopleInformation.age) {
                                                in 0..12 -> AgeDescStates.Child
                                                in 13..19 -> AgeDescStates.Teen
                                                in 20..35 -> AgeDescStates.Adult
                                                in 36..60 -> AgeDescStates.Older
                                                else -> AgeDescStates.Older
                                            }
                                        )
                                    }
                                }
                            }

                            // Zodiac Information Section
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .align(Alignment.Start),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Celebration,
                                            "",
                                            modifier = Modifier
                                                .background(
                                                    color = MaterialTheme.colorScheme.primary,
                                                    shape = CircleShape
                                                )
                                                .padding(5.dp),
                                            tint = MaterialTheme.colorScheme.onPrimary
                                        )

                                        Text(
                                            peopleInformation.peopleName +"'in :"+ stringResource(R.string.label_people_zodiac),
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                        )
                                    }

                                    FlowRow(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        ZodiacStatus.entries.filter { it != ZodiacStatus.None }.forEach { zodiac ->
                                            ColoredCheckBox(
                                                modifier = Modifier,
                                                text = stringResource(zodiac.nameRes),
                                                selectedValue = peopleInformation.zodiac == zodiac,
                                                prefixImage = zodiac.iconRes
                                            ) {
                                                peopleInformation = peopleInformation.copy(zodiac =zodiac )
                                            }
                                        }
                                     }
                                }
                            }

                            // Relationship Section
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .align(Alignment.Start),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AttachFile,
                                            "",
                                            modifier = Modifier
                                                .background(
                                                    color = MaterialTheme.colorScheme.primary,
                                                    shape = CircleShape
                                                )
                                                .padding(5.dp),
                                            tint = MaterialTheme.colorScheme.onPrimary
                                        )

                                        Text(
                                            stringResource(R.string.label_user_releationship),
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                        )
                                    }

                                    FlowRow(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                            TypeRelationship.entries.forEach { relationship ->
                                                val isSelected = peopleInformation.relationship == relationship
                                                ColoredCheckBox(
                                                    modifier = Modifier,
                                                    text = stringResource(relationship.stringRes),
                                                    selectedValue = isSelected,
                                                ) {
                                                    peopleInformation = peopleInformation.copy(relationship = relationship)
                                                }
                                            }
                                    }
                                }
                            }
                        }

                        // Page 1: Character
                        PeopleDetailsDividerState.Characteristics -> {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .align(Alignment.Start),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Celebration,
                                            "",
                                            modifier = Modifier
                                                .background(
                                                    color = MaterialTheme.colorScheme.primary,
                                                    shape = CircleShape
                                                )
                                                .padding(5.dp),
                                            tint = MaterialTheme.colorScheme.onPrimary
                                        )

                                        Text(
                                            stringResource(R.string.label_people_character),
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                        )
                                    }

                                    TextField(
                                        value = peopleInformation.bestSide,
                                        onValueChange = {peopleInformation = peopleInformation.copy(bestSide = it) },
                                        colors = TextFieldDefaults.colors(
                                            disabledIndicatorColor = Color.Transparent,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            focusedContainerColor = Color.Transparent,
                                        ),
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = MaterialTheme.shapes.small,
                                        label = {
                                            Text( peopleInformation.peopleName + "'in " + stringResource(R.string.label_people_best_side))
                                         }
                                    )

                                    FlowRow(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        val sortedCharacter = remember(peopleInformation.character.toList()) {
                                            CharacterTrait.entries.sortedByDescending { peopleInformation.character.contains(it) }
                                        }

                                        sortedCharacter.forEach { character ->
                                            val isSelected = peopleInformation.character.contains(character)
                                            ColoredCheckBox(
                                                modifier = Modifier,
                                                text = stringResource(character.textSource),
                                                selectedValue = isSelected,
                                            ) {
                                                val updatedCharacter = if (isSelected) {
                                                    peopleInformation.character - character
                                                } else {
                                                    peopleInformation.character + character
                                                }
                                                peopleInformation = peopleInformation.copy(character = updatedCharacter)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Page 2: Hobbies
                        PeopleDetailsDividerState.Hobbies -> {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AcUnit,
                                            "",
                                            modifier = Modifier
                                                .background(
                                                    color = MaterialTheme.colorScheme.primary,
                                                    shape = CircleShape
                                                )
                                                .padding(5.dp),
                                            tint = MaterialTheme.colorScheme.onPrimary
                                        )
                                        Text(
                                            text = stringResource(R.string.label_people_hobies),
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                        )
                                    }

                                    FlowRow(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        val sortedHobbies = remember(peopleInformation.hobbies.toList()) {
                                            Hobbies.entries.sortedByDescending { peopleInformation.hobbies.contains(it) }
                                        }
                                        sortedHobbies.forEach { hobbies ->
                                            val isSelected = peopleInformation.hobbies.contains(hobbies)
                                            ColoredCheckBox(
                                                modifier = Modifier,
                                                text = stringResource(hobbies.textSource),
                                                selectedValue = isSelected,
                                                prefixImage = hobbies.iconSource
                                            ) {
                                                val updatedHobbies = if (isSelected) {
                                                    peopleInformation.hobbies - hobbies
                                                } else {
                                                    peopleInformation.hobbies + hobbies
                                                }
                                                peopleInformation = peopleInformation.copy(hobbies = updatedHobbies)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = isInformationChange.value,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Button(
                onClick = {
                    updatePeopleInformation(peopleInformation)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(paddingSave),
            ){
                Text(
                    text = stringResource(R.string.save_text),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

    }
}
