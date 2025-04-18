package com.example.aklimdakihediye.ObserverClasses

import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aklimdakihediye.ViewModels.AboutGiftInformationViewModel
import com.example.aklimdakihediye.ViewModels.SpecialGiftViewModel

sealed class ViewModelState {
    data class AboutGiftViewModel (val viewModel : AboutGiftInformationViewModel) : ViewModelState()
    data class SpecialRowViewModel(val viewModel: SpecialGiftViewModel) : ViewModelState()
}