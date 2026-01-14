package com.reylortechnology.aklimdakihediye.models.ComposeModels

import androidx.compose.runtime.MutableState
import org.checkerframework.checker.units.qual.Prefix

data class UserSettingsTextDataModel(
    val prefix: String,
    val value : MutableState<String>
)