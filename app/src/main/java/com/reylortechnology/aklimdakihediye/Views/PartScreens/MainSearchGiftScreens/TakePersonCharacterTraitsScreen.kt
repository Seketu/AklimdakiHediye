package com.reylortechnology.aklimdakihediye.Views.PartScreens.MainSearchGiftScreens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.reylortechnology.aklimdakihediye.Compose.ColoredCheckBox
import com.reylortechnology.aklimdakihediye.Compose.clearIndicatorColors
import com.reylortechnology.aklimdakihediye.ObserverClasses.ZodiacStatus
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.models.Enums.CharacterTrait
import com.reylortechnology.aklimdakihediye.ui.theme.poppinsHeadline
import kotlin.collections.filter

@Composable
fun TakePersonCharacterTraitsScreen(
    modifier: Modifier = Modifier,
    bestSideState: MutableState<String>,
    characterTraits: MutableList<CharacterTrait>,
    zodiacStatus: MutableState<ZodiacStatus>,
    peopleName : String,
    onNext: (Boolean) -> Unit = { }
) {

    val isSave = remember {
        mutableStateOf(false)
    }

    val sortedList = remember(characterTraits) {
        CharacterTrait.entries.sortedByDescending { characterTraits.contains(it) }
    }

    val buttonOpen = remember(characterTraits, zodiacStatus.value, bestSideState.value) {
        characterTraits.isNotEmpty() && zodiacStatus.value != ZodiacStatus.None && bestSideState.value.isNotBlank()
    }

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    "Kişinin karakter özelliklerini seçin",
                    style = MaterialTheme.typography.poppinsHeadline.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    "Bu bilgiler, kişiye en uygun hediyeleri bulmamıza yardımcı olacak.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.5f
                    )
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isSave.value,
                    onCheckedChange = { isSave.value = it },
                )
                Text(
                    "$peopleName'i Kaydetmek istermisiniz ?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.5f
                    )
                )
            }
            TextField(
                value = bestSideState.value,
                onValueChange = { bestSideState.value = it },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TextFieldDefaults.clearIndicatorColors(),
                label = {
                    Text(
                        "Kişinin en iyi yönleri nelerdir?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.5f
                        )
                    )
                },
                shape = MaterialTheme.shapes.medium,
                singleLine = false,
                maxLines = 4
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "5",
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        stringResource(R.string.label_people_character),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                    )
                }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    sortedList.forEach { trait ->

                        ColoredCheckBox(
                            text = stringResource(trait.textSource),
                            selectedValue = characterTraits.contains(trait),
                            onCheckedChange = {
                                if (it) {
                                    characterTraits.add(trait)
                                } else {
                                    characterTraits.remove(trait)
                                }
                            }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "6",
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        stringResource(R.string.label_people_zodiac),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                    )
                }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ZodiacStatus.entries.filter { it != ZodiacStatus.None }
                        .forEach { zodiac ->

                            ColoredCheckBox(
                                text = stringResource(zodiac.nameRes),
                                selectedValue = zodiacStatus.value == zodiac,
                                prefixImage = zodiac.iconRes,
                                onCheckedChange = {
                                    if (it) {
                                        zodiacStatus.value = zodiac
                                    } else {
                                        zodiacStatus.value = ZodiacStatus.None
                                    }
                                }
                            )
                        }
                }
            }
            AnimatedContent(
                targetState = buttonOpen,
            ) {
                if (it) {
                    Button(
                        onClick = {
                            onNext(isSave.value)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.20f),
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

@Preview
@Composable
private fun TakePersonCharacterTraitsPrev() {
    val bestSide = remember {
        mutableStateOf("")
    }
    val list = remember {
        mutableStateListOf<CharacterTrait>(
        )
    }
    val zodiacStatus = remember {
        mutableStateOf(ZodiacStatus.Sagittarius)
    }
    list.add(CharacterTrait.STUBBORNNESS)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.surface
            )
    ) {
        TakePersonCharacterTraitsScreen(
            bestSideState = bestSide,
            characterTraits = list,
            zodiacStatus = zodiacStatus,
            peopleName = "Ahmet"
        )
    }
}