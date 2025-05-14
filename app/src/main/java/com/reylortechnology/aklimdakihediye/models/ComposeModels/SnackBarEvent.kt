package com.reylortechnology.aklimdakihediye.models.ComposeModels

sealed class SnackBarEvent {
    object None : SnackBarEvent()
    data class ShowSnackBar(val message: String) : SnackBarEvent()
}