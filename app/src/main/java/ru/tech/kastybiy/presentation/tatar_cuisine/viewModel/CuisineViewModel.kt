package ru.tech.kastybiy.presentation.tatar_cuisine.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.tech.kastybiy.core.Action
import ru.tech.kastybiy.domain.use_case.cuisine.get_cuisine.GetCuisineUseCase
import ru.tech.kastybiy.presentation.tatar_cuisine.components.CuisineState
import javax.inject.Inject

@HiltViewModel
class CuisineViewModel @Inject constructor(
    private val getCuisineUseCase: GetCuisineUseCase
) : ViewModel() {

    private val _cuisineState = mutableStateOf(CuisineState())
    val cuisineState: State<CuisineState> = _cuisineState

    init {
        getCuisine()
    }

    private fun getCuisine() {
        getCuisineUseCase().onEach { result ->
            when (result) {
                is Action.Success -> {
                    _cuisineState.value = CuisineState(cuisineList = result.data)
                }
                is Action.Empty -> {
                    _cuisineState.value = CuisineState(
                        error = result.message ?: "Нәрсәдер начар булып чыккан"
                    )
                }
                is Action.Loading -> {
                    _cuisineState.value = CuisineState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun reload() {
        getCuisine()
    }

}