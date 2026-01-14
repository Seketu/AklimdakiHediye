package com.reylortechnology.aklimdakihediye.ViewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.LocalUserInformation
import com.reylortechnology.aklimdakihediye.NavController.LocalNavController
import com.reylortechnology.aklimdakihediye.ObserverClasses.ForWhoObserver
import com.reylortechnology.aklimdakihediye.ObserverClasses.AlertDialogObserver
import com.reylortechnology.aklimdakihediye.ObserverClasses.SpecialRowStatus
import com.reylortechnology.aklimdakihediye.ObserverClasses.ToScreenObserver
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.Repo.MainRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserMainViewModel
    @Inject constructor(
        val mainRepo: MainRepo,
        val context: Context,
    ) : ViewModel() {

    private var _rowStatus = MutableStateFlow<SpecialRowStatus>(SpecialRowStatus.None)
    var rowStatus = _rowStatus.asStateFlow()

    private var _alertDialogState = MutableStateFlow<AlertDialogObserver>(AlertDialogObserver.none)
    var alertDialogState = _alertDialogState.asStateFlow()

    private var _userInformation = MutableStateFlow<List<LocalUserInformation>>(emptyList())
    val userInformation = _userInformation.asStateFlow()

    private var _showAlert = MutableStateFlow<Boolean>(true)
    val showAlert = _showAlert.asStateFlow()

    private var _toInfoScreen = MutableStateFlow<ToScreenObserver>(ToScreenObserver.none)
    val infoScreen = _toInfoScreen.asStateFlow()

    private var _forWhoState = MutableStateFlow<ForWhoObserver>(ForWhoObserver.none)
    var forWhoState = _forWhoState.asStateFlow()

    init {
        viewModelScope.launch {
            mainRepo.getUserInformation.apply {
                collect {
                    _userInformation.value = it
                    Log.e("Error",it.toString())
                    checkUserInformation()
                }
            }.catch {
                Log.e("Error",it.message.toString())
            }
        }
    }

    fun updateAlertStateForRowNavigation(
        forWho : String,
        source: Int
    ){
        if(userInformation.value.isEmpty()){
            _showAlert.value = true
            _alertDialogState.value = AlertDialogObserver.NewAlertDialog(
                title = "Kullanıcı Bilgisi Boş Devam etmek için kendinizi tanıtın",
                onDismiss = {
                    _showAlert.value = !_showAlert.value
                    _alertDialogState.value = AlertDialogObserver.none
                },
                onConfirm = {
                    _toInfoScreen.value = ToScreenObserver.NewInformation(forDay = null, source = null)
                },
                dismissButton = {
                    _showAlert.value = !_showAlert.value
                    _alertDialogState.value = AlertDialogObserver.none
                },
                dismissText = "Vazgeç",
                confirmText = "Bilgilerimi Ekle",
            )
        }else{
            _showAlert.value = true
            _alertDialogState.value = AlertDialogObserver.NewAlertDialog(
                title = context.getString(R.string.for_who_user_label),
                onDismiss = {
                    _showAlert.value = !_showAlert.value
                    _alertDialogState.value = AlertDialogObserver.none
                },
                dismissButton = {
                    _rowStatus.value = SpecialRowStatus.ToTakeInformation(
                        forAnother = true,
                        forWho = forWho,
                        source = source
                    )
                },
                onConfirm = {
                    _rowStatus.value = SpecialRowStatus.ToScreen(forWho = forWho,source = source)
                },
                dismissText = context.getString(R.string.for_who_another_label),
                confirmText = context.getString(R.string.for_who_forme_label),
            )
        }
    }

    fun checkUserInformation() {
        if (_userInformation.value.isEmpty()) {
            _alertDialogState.value = AlertDialogObserver.NewAlertDialog(
                title = context.getString(R.string.alert_dialog_empty_info_title),
                onDismiss = {
                    _showAlert.value = !_showAlert.value
                },
                onConfirm = {
                    _toInfoScreen.value = ToScreenObserver.NewInformation(forDay = null, source = null)
                },
                dismissText = context.getString(R.string.alert_dialog_empty_info_dismiss),
                confirmText = context.getString(R.string.alert_dialog_empty_info_confirm),
                dismissButton = {
                    _showAlert.value = !_showAlert.value
                }
            )
        } else {
            _alertDialogState.value = AlertDialogObserver.none
        }
    }

    fun updateForWhoState(forWhoObserver: ForWhoObserver,context: Context,forDay : String,source : Int){

        val userInformation = userInformation.value
        if (userInformation.isEmpty()){
            _toInfoScreen.value = ToScreenObserver.NewInformation(forDay = forDay,source = source)
        }else{
            _toInfoScreen.value = ToScreenObserver.WithUserInformation(forDay,source)
        }

        /*
        Depreceted For check another user
        when(forWhoObserver){
            is ForWhoObserver.checkState -> {
                _showAlert.value = true
                _alertDialogState.value = AlertDialogObserver.NewAlertDialog(
                    title = context.getString(R.string.for_who_user_label),
                    onDismiss = {
                        _showAlert.value = !_showAlert.value
                        _alertDialogState.value = AlertDialogObserver.none
                    },
                    onConfirm = {

                    },
                    dismissText = context.getString(R.string.for_who_another_label),
                    confirmText = context.getString(R.string.for_who_forme_label),
                    dismissButton = {
                        _showAlert.value = !_showAlert.value
                        _alertDialogState.value = AlertDialogObserver.none
                    }
                )
            }
            ForWhoObserver.none -> {

            }
        }*/
    }

    fun navigateFromColumn(
        navController: NavController,
        forDay: String?,
        source: Int?,
        forWhat: String,
    ){
        when(forWhat){
            "SaveInf" -> {
                navController.navigate(LocalNavController.UserInfoScreen(forDay,source,forWhat))
                _toInfoScreen.value = ToScreenObserver.none
            }
            "WithInf" -> {
                if (forDay.isNullOrEmpty() && source == null){
                    Log.e("Error At UserMainViewModel","forDay is null or empty")
                }else{
                    navController.navigate(LocalNavController.DailyInfoScreen(forDay!!,source!!,false))
                    _toInfoScreen.value = ToScreenObserver.none
                }
            }
        }
    }
    fun clearNavigationEvent() {
        _toInfoScreen.value = ToScreenObserver.none
    }

}