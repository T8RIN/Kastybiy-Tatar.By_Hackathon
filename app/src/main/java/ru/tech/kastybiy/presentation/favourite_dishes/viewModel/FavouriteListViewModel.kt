package ru.tech.kastybiy.presentation.favourite_dishes.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.tech.kastybiy.core.Action
import ru.tech.kastybiy.domain.use_case.cuisine.get_favourites.GetFavouriteDishesUseCase
import ru.tech.kastybiy.presentation.tatar_cuisine.components.CuisineState
import javax.inject.Inject

@HiltViewModel
class FavouriteListViewModel @Inject constructor(
    private val getFavouriteDishesUseCase: GetFavouriteDishesUseCase
) : ViewModel() {

    private val _favState = mutableStateOf(CuisineState())
    val favState: State<CuisineState> = _favState

    init {
        getFavourites()
    }

    private fun getFavourites() {
        getFavouriteDishesUseCase().onEach { result ->
            when (result) {
                is Action.Success -> {
                    _favState.value = CuisineState(cuisineList = result.data)
                }
                is Action.Empty -> {
                    _favState.value = CuisineState(
                        error = result.message ?: "Нәрсәдер начар булып чыккан"
                    )
                }
                is Action.Loading -> {
                    _favState.value = CuisineState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun reload() {
        getFavourites()
    }

}