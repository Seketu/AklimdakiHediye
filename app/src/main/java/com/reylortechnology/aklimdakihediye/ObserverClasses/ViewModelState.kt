package com.reylortechnology.aklimdakihediye.ObserverClasses

import com.reylortechnology.aklimdakihediye.ViewModels.AboutGiftInformationViewModel
import com.reylortechnology.aklimdakihediye.ViewModels.SpecialGiftViewModel

sealed class ViewModelState {
    data class AboutGiftViewModel (val viewModel : AboutGiftInformationViewModel) : ViewModelState()
    data class SpecialRowViewModel(val viewModel: SpecialGiftViewModel) : ViewModelState()
}