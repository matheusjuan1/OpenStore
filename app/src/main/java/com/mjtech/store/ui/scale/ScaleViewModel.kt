package com.mjtech.store.ui.scale

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjtech.acbrlib.bal.data.repository.BalConfigRepository
import com.mjtech.store.domain.common.Result
import com.mjtech.store.domain.scale.repository.PricingRepository
import com.mjtech.store.domain.scale.repository.ScaleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScaleViewModel(
    private val pricingRepository: PricingRepository,
    private val acbrLibBalRepository: ScaleRepository,
    private val balConfigRepository: BalConfigRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScaleUiState())
    val uiState: StateFlow<ScaleUiState> = _uiState

    val initialLoading: StateFlow<Boolean> = _uiState
        .map { state ->
            val priceDone = state.price !is Result.Loading
            val initializedDone = state.initialize !is Result.Loading
            val activatedDone = state.activate !is Result.Loading

            val initialTasksDone = priceDone && initializedDone && activatedDone

            !initialTasksDone
        }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    init {
        initialize()
        getPriceSetting()
    }

    fun getWeight() {
        viewModelScope.launch {
            acbrLibBalRepository.getWeight().collect { weightResult ->
                _uiState.update { currentState ->
                    currentState.copy(weight = weightResult)
                }
            }
        }
    }

    private fun getPriceSetting() {
        viewModelScope.launch {
            pricingRepository.getPriceSetting().collect { priceResult ->
                _uiState.update { currentState ->
                    currentState.copy(price = priceResult)
                }
            }
        }
    }

    private fun initialize() {
        viewModelScope.launch {
            balConfigRepository.inicializar().collect { inicializarResult ->
                _uiState.update { currentState ->
                    currentState.copy(initialize = inicializarResult)
                }
            }
        }
    }

    fun activate() {
        viewModelScope.launch {
            balConfigRepository.ativar().collect { ativarResult ->
                _uiState.update { currentState ->
                    currentState.copy(activate = ativarResult)
                }
            }
        }
    }

    private fun deactivate() {
        viewModelScope.launch {
            balConfigRepository.desativar().collect { desativarResult ->
                _uiState.update { currentState ->
                    currentState.copy(deactivate = desativarResult)
                }
            }
        }
    }

    private fun finish() {
        viewModelScope.launch {
            balConfigRepository.finalizar().collect { finalizarResult ->
                _uiState.update { currentState ->
                    currentState.copy(finish = finalizarResult)
                }
            }
        }
    }
}