package com.mjtech.store.ui.scale

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjtech.store.domain.scale.repository.PricingRepository
import com.mjtech.store.domain.scale.repository.ScaleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScaleViewModel(
    private val pricingRepository: PricingRepository,
    private val acbrLibBalRepository: ScaleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScaleUiState())
    val uiState: StateFlow<ScaleUiState> = _uiState

    init {
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
}