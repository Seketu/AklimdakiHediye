package com.reylortechnology.aklimdakihediye.Views.PartScreens.PeoplesViewScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.reylortechnology.aklimdakihediye.Compose.PeopleListCard
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Peoples
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.models.Enums.PeopleUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeoplesMainScreen(
    modifier: Modifier = Modifier,
    peoplesData : List<PeopleUiModel>,
    onEdit : (Peoples) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            if (peoplesData.isEmpty()){
                Text(
                    text = stringResource(R.string.peoples_screen_no_people),
                    style = MaterialTheme.typography.headlineMedium
                )
            }else{
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(top = 16.dp)
                ) {
                    items(
                        items = peoplesData
                    ){people ->
                        PeopleListCard(
                            people = people.people,
                            isBirthdayNote = people.isBirthdayNote,
                            isSpecialDayNote = people.isSpecialNote,
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .height(150.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.large)
                                .border(2.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.large),
                            textColor = MaterialTheme.colorScheme.onSurface,
                            onAction = {
                              onEdit(people.people)
                            }
                        )
                    }
                }
            }

        }
    }
}
