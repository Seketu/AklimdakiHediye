package com.reylortechnology.aklimdakihediye.ObserverClasses

import com.reylortechnology.aklimdakihediye.ViewModels.CelebretadDayViewModel
import com.reylortechnology.aklimdakihediye.ViewModels.SpecialGiftViewModel

sealed class ViewModelState {
    data class AboutGiftViewModel (val viewModel : CelebretadDayViewModel) : ViewModelState()
    data class SpecialRowViewModel(val viewModel: SpecialGiftViewModel) : ViewModelState()
}