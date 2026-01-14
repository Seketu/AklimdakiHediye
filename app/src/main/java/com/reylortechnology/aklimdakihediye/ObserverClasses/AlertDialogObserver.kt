package com.reylortechnology.aklimdakihediye.ObserverClasses

sealed class AlertDialogObserver {
    data class NewAlertDialog(
        val title: String,
        val onDismiss: () -> Unit,
        val onConfirm: () -> Unit,
        val dismissText: String,
        val confirmText: String,
        val dismissButton: () -> Unit
    ) : AlertDialogObserver()
    object none : AlertDialogObserver()
}

sealed class ToScreenObserver{
    data class NewInformation(val forDay : String?,val source : Int?) : ToScreenObserver()
    data class WithUserInformation(val forDay: String,val source: Int) : ToScreenObserver()
    object none : ToScreenObserver()
}