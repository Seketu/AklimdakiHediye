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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
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
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.models.Enums.Hobbies
import com.reylortechnology.aklimdakihediye.ui.theme.poppinsFontFamily

@Composable
fun TakePersonHobbiesScreen(
        modifier: Modifier = Modifier,
        peopleName : String = "",
        peopleJob : MutableState<String>,
        peopleHobbies : MutableList<Hobbies>,
        onNext: () -> Unit,
) {
        val buttonOpen = remember {
                derivedStateOf {
                        peopleJob.value.isNotEmpty() && peopleHobbies.isNotEmpty()
                }
        }

        val hobbiesList = remember(peopleHobbies) {
                Hobbies.entries.sortedByDescending { peopleHobbies.contains(it) }
        }

        Box(
                modifier = modifier
        ){
                Column(
                        modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 10.dp, vertical = 10.dp)
                                .verticalScroll(rememberScrollState())
                                .imePadding(),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                        Column(
                                modifier = Modifier
                                        .fillMaxWidth(),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                                Text(
                                        "${peopleName.uppercase()} Neler ile İlgilenmeyi Sever ?",
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                fontFamily = poppinsFontFamily,
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface,
                                )
                                Text(
                                        "Yapay zekamızın mükemmel eşleşmeyi bulmasına yardımcı olmak için ilgi alanlarını seçin veya kendi etiketlerinizi ekleyin.",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                                fontFamily = poppinsFontFamily,
                                                fontWeight = FontWeight.W600
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface.copy(
                                                alpha = 0.5f
                                        ),
                                )
                        }

                        TextField(
                                colors = TextFieldDefaults.clearIndicatorColors(),
                                value = peopleJob.value,
                                onValueChange = { peopleJob.value = it },
                                modifier = Modifier
                                        .fillMaxWidth(),
                                label =  {
                                        Text(
                                                "Mesleği Nedir ?",
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontFamily = poppinsFontFamily,
                                                        fontWeight = FontWeight.W600
                                                ),
                                                color = MaterialTheme.colorScheme.onSurface.copy(
                                                        alpha = 0.5f
                                                 )
                                        )
                                },
                                shape = MaterialTheme.shapes.small
                        )

                        Column(
                                modifier = Modifier
                                        .fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                                Row(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                ) {
                                        Text(
                                                "4",
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
                                                stringResource(R.string.label_people_hobies),
                                                style = MaterialTheme.typography.labelLarge.copy(
                                                        fontWeight = FontWeight.Bold
                                                ),
                                        )
                                }

                                FlowRow(
                                        verticalArrangement = Arrangement.spacedBy(10.dp),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier
                                                .fillMaxWidth()
                                ) {
                                        hobbiesList.forEach {hobbies ->
                                                ColoredCheckBox(
                                                        text = stringResource(hobbies.textSource),
                                                        prefixImage = hobbies.iconSource,
                                                        selectedValue = peopleHobbies.contains(hobbies),
                                                        modifier = Modifier
                                                ) {
                                                        if (it){
                                                                peopleHobbies.add(hobbies)
                                                        }else {
                                                                peopleHobbies.remove(hobbies)
                                                        }
                                                }
                                        }
                                }
                        }

                        AnimatedContent(
                                targetState = buttonOpen.value
                        ) {
                                if (it){
                                        Button(
                                                onClick = {
                                                        onNext()
                                                },
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

@Preview
@Composable
private fun TakePersonHobbiesPrev() {
        Box(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
        ){
                val assa = remember {
                        mutableStateOf("")
                }
                TakePersonHobbiesScreen(
                        peopleName = "Ahmet",
                        peopleJob = assa,
                        peopleHobbies = mutableListOf(),

                ){
                }
        }

}