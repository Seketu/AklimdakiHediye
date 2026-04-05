package com.reylortechnology.aklimdakihediye.Compose

import android.content.Intent
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.SavedGifts
import com.reylortechnology.aklimdakihediye.ObserverClasses.RelationshipStatus
import com.reylortechnology.aklimdakihediye.ui.theme.onSurfaceLight
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import androidx.core.net.toUri
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Peoples
import com.reylortechnology.aklimdakihediye.ObserverClasses.ZodiacStatus
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.models.Enums.AgeDescStates
import com.reylortechnology.aklimdakihediye.models.Enums.CharacterTrait
import com.reylortechnology.aklimdakihediye.models.Enums.Genders
import com.reylortechnology.aklimdakihediye.models.Enums.Hobbies
import com.reylortechnology.aklimdakihediye.models.Enums.TypeRelationship
import com.reylortechnology.aklimdakihediye.ui.theme.OleoScript


@Composable
fun AgeDescSurface(
    modifier: Modifier = Modifier,
    ageState : AgeDescStates,
) {

    val targetColor = ageState.color

    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 500), // 500ms sürede renk değişsin
        label = "ColorAnimation"
    )
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(5.dp,Color.White),
        shadowElevation = 5.dp,
        color = animatedColor
    ) {
        AnimatedContent(
            targetState = ageState,
            transitionSpec = {
                (slideInVertically { height-> height } + fadeIn()).togetherWith(
                    slideOutVertically { fullHeight -> fullHeight } + fadeOut()
                )
            }
        ) {
            when(it){
                AgeDescStates.Child -> {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            painter = painterResource(it.iconSource),
                            "",
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .aspectRatio(1f),
                        )
                        AutoResizeText(
                            text = stringResource(it.text),
                            modifier = Modifier
                                .weight(1f),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }
                }
                AgeDescStates.Teen -> {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            painter = painterResource(it.iconSource),
                            "",
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .aspectRatio(1f),
                        )
                        AutoResizeText(
                            text = stringResource(it.text),
                            modifier = Modifier
                                .weight(1f),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White

                        )
                    }
                }
                AgeDescStates.Adult -> {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            painter = painterResource(it.iconSource),
                            "",
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .aspectRatio(1f),
                        )
                        AutoResizeText(
                            text = stringResource(it.text),
                            modifier = Modifier
                                .weight(1f),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }
                }
                AgeDescStates.Older -> {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            painter = painterResource(it.iconSource),
                            "",
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .aspectRatio(1f),
                        )
                        AutoResizeText(
                            text = stringResource(it.text),
                            modifier = Modifier
                                .weight(1f),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun PeopleListCard(
    modifier: Modifier = Modifier,
    people : Peoples,
    isBirthdayNote : String? = null,
    isSpecialDayNote : String? = null,
    onAction : () -> Unit = {},
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Row(
        modifier = modifier
            .padding(3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth(0.2f)
                .aspectRatio(1f)
                .border(
                    width =  1.dp,
                    shape = CircleShape,
                    color = people.color
                )
                .padding(1.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ){
            Image(
                painter = painterResource(id = people.image),
                contentDescription = "People Image",
                modifier = Modifier,
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                people.peopleName,
                color = textColor,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = OleoScript
                )
            )
            Text(
                stringResource(people.relationship.stringRes),
                color = textColor,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Thin
                )
            )
                if (isBirthdayNote != null){
                    Text(
                        isBirthdayNote,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
                if (isSpecialDayNote != null){
                    Text(
                        isSpecialDayNote,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier.background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(5.dp)
                        ).padding(horizontal = 5.dp, vertical = 2.dp),
                    )
                }
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
        ){
            IconButton(
                modifier = Modifier
                    .align(Alignment.Center),
                onClick = {
                    onAction()
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Düzenle",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(45.dp)
                )
            }
        }
    }
}

@Composable
fun StepperIndicator(currentStep: Int, modifier: Modifier, totalSteps: Int) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
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
fun ReminderCard(
    modifier: Modifier = Modifier,
    title: String,
    event : String,
    time : Long,
    releationship : String
) {

    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("tr"))
    sdf.timeZone = TimeZone.getTimeZone("Europe/Istanbul")

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                title,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Text(
                event,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                releationship,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSecondary
            )

            Text(
                sdf.format(Date(time)),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerView(
    selectedDateMillis1: MutableState<Long?>,
    modifier: Modifier = Modifier
) {
    val showDatePickerDialog = remember { mutableStateOf(false) }
    Column(
        modifier
    ) {
        Button(
            onClick = { showDatePickerDialog.value = true },
            modifier = Modifier
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.small
        ) {
            Text("Tarih Seç")
        }
        // 2. Seçilen tarihi göster
        selectedDateMillis1.value?.let { millis ->
            val formattedDate = remember(millis) {
                SimpleDateFormat("dd MMMM yyyy", Locale("tr")).format(Date(millis))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Seçilen Tarih: $formattedDate",
                fontSize = 23.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(5.dp)
            )
        }
    }
    // 3. DatePicker Dialog
    if (showDatePickerDialog.value) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedDateMillis1.value = datePickerState.selectedDateMillis
                        showDatePickerDialog.value = false
                    }
                ) {
                    Text("Tamam")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerDialog.value = false }) {
                    Text("İptal")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}


@Composable
fun MainPeopleCard(
    modifier: Modifier = Modifier,
    peoples: Peoples
) {
    val context  = LocalContext.current

    val hataliDosyaAdi = context.resources.getResourceEntryName(peoples.image)
    Log.d(
        "PeopleImageId",
        peoples.image.toString() + " - " + hataliDosyaAdi
    )
    val darkTheme = isSystemInDarkTheme()
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
                .border(
                    8.dp,
                    brush = Brush.radialGradient(
                        0.7f to peoples.color,
                        1f to peoples.color.copy(alpha = 0.1f)
                    ),
                    shape = CircleShape
                )
                .padding(8.dp)
                .clip(CircleShape)
        ){
            Image(
                painter = painterResource(id = peoples.image),
                contentDescription = "People Image",
                modifier = Modifier,
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = peoples.peopleName,
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 20.sp),
            color = if (darkTheme) Color.White else Color.Black
        )
    }
}

@Composable
fun AddMainPeopleCard(
    modifier : Modifier = Modifier,
){
    val darkTheme = isSystemInDarkTheme()
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .weight(1f)
                .clip(CircleShape)
                .drawBehind {
                    val stroke = Stroke(
                        width = 10f,
                        pathEffect = PathEffect.dashPathEffect(
                            intervals = floatArrayOf(15f, 10f),
                            phase = 0f
                        )
                    )
                    drawOutline(
                        outline = CircleShape.createOutline(size, layoutDirection, this),
                        color = if (darkTheme) Color.White else Color.Black,
                        style = stroke
                    )

                },
            contentAlignment = Alignment.Center
        ){
            Text("+",
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                fontSize = 50.sp,
                textAlign = TextAlign.Center
                )
        }
        Text(
            text = stringResource(R.string.new_label),
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = if (darkTheme) Color.White else Color.Black
        )
    }
}
@Composable
fun SpecialDayRelationshipCard(
    modifier: Modifier = Modifier,
    videoSource: Int,
    title: String,
    description: String,
    checked: State<Boolean>,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = modifier
            .border(
                3.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium
            )
            .background(
                MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.medium
            )
            .clickable {
                onCheckedChange.invoke(!checked.value)
            }
    ){
        LottieAnim(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(85.dp),
            source = videoSource,
            contentScale = ContentScale.FillWidth,
        )
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(5.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    title,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = 15.sp
                )
                Spacer(Modifier.weight(1f))
                Checkbox(
                    checked = checked.value,
                    onCheckedChange = onCheckedChange,
                    enabled = true,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.onSecondary,
                        checkmarkColor = MaterialTheme.colorScheme.onPrimary,
                    )
                )
            }
            Text(
                description,
                fontSize = 9.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun ReleationshipStateInfo(
    modifier: Modifier = Modifier,
    selectedOption: MutableState<RelationshipStatus>,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .background(Color.Transparent)
            .fillMaxWidth(0.9f)
    ) {
        Text("İlişki Durumu")

        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(5.dp)
                    .clickable(
                        onClick = {
                            selectedOption.value = RelationshipStatus.MARRIED
                        })
                    .background(Color.White, RoundedCornerShape(10.dp))
            ) {
                Checkbox(
                    checked = selectedOption.value == RelationshipStatus.MARRIED,
                    enabled = true,
                    onCheckedChange = {
                        if (it) selectedOption.value =
                            RelationshipStatus.MARRIED else selectedOption.value =
                            RelationshipStatus.NONE
                    })
                Text("Evli",color = Color.Black)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(5.dp)
                    .clickable(
                        onClick = {
                            selectedOption.value = RelationshipStatus.SweatHeart
                        })
                    .background(Color.White, RoundedCornerShape(10.dp))
            ) {

                Checkbox(
                    checked = selectedOption.value == RelationshipStatus.SweatHeart,
                    enabled = true,
                    onCheckedChange = {
                        if (it) selectedOption.value =
                            RelationshipStatus.SweatHeart else RelationshipStatus.NONE
                    })
                Text("Sevgili",color = Color.Black)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(5.dp)
                    .clickable(
                        onClick = {
                            selectedOption.value = RelationshipStatus.FLORT
                        })
                    .background(Color.White, RoundedCornerShape(10.dp))
            ) {
                Checkbox(
                    checked = selectedOption.value == RelationshipStatus.FLORT,
                    enabled = true,
                    onCheckedChange = {
                        if (it) selectedOption.value =
                            RelationshipStatus.FLORT else RelationshipStatus.NONE
                    })
                Text("Flört",color = Color.Black)
            }
        }
    }
    Spacer(Modifier.height(25.dp))
}



@Composable
fun SavedGiftCard(
    modifier: Modifier = Modifier,
    gift: SavedGifts,
    checked: MutableState<Boolean>,
    onCheckedChange: (Boolean) -> Unit,
    enabled : Boolean
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (!enabled) {
                    // Normal mode - go to gift URL
                    val intent = Intent(Intent.ACTION_VIEW, gift.giftUrl.toUri())
                    context.startActivity(intent)
                }
                // Delete mode'da card click'i checkbox'ı handle etmesin
            },
        colors = CardDefaults.cardColors(
            containerColor = if (checked.value && enabled) {
                MaterialTheme.colorScheme.errorContainer
            } else {
                MaterialTheme.colorScheme.surfaceContainer
            }
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (checked.value && enabled) 8.dp else 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gift icon
            Card(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Gift details
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = gift.giftName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (checked.value && enabled) {
                        MaterialTheme.colorScheme.onErrorContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (!enabled) {
                    Text(
                        text = "Sayfaya git",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Checkbox or action icon
            if (enabled) {
                AnimatedVisibility(
                    visible = true,
                    enter = scaleIn(animationSpec = spring()) + fadeIn(),
                    exit = scaleOut(animationSpec = spring()) + fadeOut()
                ) {
                    Checkbox(
                        checked = checked.value,
                        onCheckedChange = { newValue ->
                            checked.value = newValue
                            onCheckedChange(newValue)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.error,
                            uncheckedColor = MaterialTheme.colorScheme.outline
                        )
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Go to page",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/*


DEPRECETED AFTER USER SETTİNGS UPDATE

@Composable
fun UserInformationPlace(
    modifier: Modifier = Modifier,
    list : MutableList<Pair<String, MutableState<String>>>,
    editEnable : MutableState<Boolean>
    ) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        list.forEach { (label, value) ->
            UserInformationRow(
                modifier = Modifier.fillMaxWidth(),
                label = label,
                value = value,
                enabled = editEnable
            )
        }
    }
}
*/
@Preview
@Composable
private fun CardPreviewAdd() {
    PeopleListCard(
        modifier = Modifier.fillMaxWidth().height(150.dp).background(MaterialTheme.colorScheme.surfaceVariant),
        people = Peoples(
            peopleName = "Reyhan",
            relationship = TypeRelationship.Coworker,
            birthday = java.time.LocalDate.now().plusDays(2),
            age = 20,
            zodiac = ZodiacStatus.Leo,
            hobbies = listOf(Hobbies.Basketball),
            bestSide = "Yardımsever",
            character = listOf(CharacterTrait.STUBBORNNESS),
            job = "Öğrenci",
            image = R.drawable.woman_2,
            color = Color(0xFFFFC107),
            gender = Genders.Female
        ),
        isSpecialDayNote = "2 gün sonra Doğum Günü"
    )
}
