package com.reylortechnology.aklimdakihediye.Interfaces.ViewModelDelegation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


interface StateManager<UiState>{
    val state : StateFlow<UiState>
    fun updateState (block : UiState.()-> UiState)
    val currentState  : UiState
}

class StateManagerImp<State>(initialState : State) : StateManager<State>{

    private val _state = MutableStateFlow(initialState)

    override val state: StateFlow<State> = _state.asStateFlow()

    override fun updateState(block: State.() -> State) {
        _state.update(block)
    }

    override val currentState: State
        get() = _state.value


}