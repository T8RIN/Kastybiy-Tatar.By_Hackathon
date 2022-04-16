package ru.tech.kastybiy.presentation.products_list.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.tech.kastybiy.core.Action
import ru.tech.kastybiy.domain.use_case.cuisine.get_fridge_list.GetFridgeListUseCase
import ru.tech.kastybiy.domain.use_case.cuisine.update_fridge.UpdateFridgeUseCase
import ru.tech.kastybiy.presentation.products_list.components.FridgeListState
import javax.inject.Inject

@HiltViewModel
class FridgeViewModel @Inject constructor(
    private val getFridgeListUseCase: GetFridgeListUseCase,
    private val updateFridgeUseCase: UpdateFridgeUseCase
) : ViewModel() {

    private val _listState = mutableStateOf(FridgeListState())
    val listState: State<FridgeListState> = _listState

    init {
        getFridgeList()
    }

    private fun getFridgeList() {
        getFridgeListUseCase().onEach { result ->
            when (result) {
                is Action.Success -> {
                    _listState.value = FridgeListState(products = result.data?.sortedBy { it.name })
                }
                is Action.Empty -> {
                    _listState.value = FridgeListState(
                        error = result.message ?: "Нәрсәдер начар булып чыккан"
                    )
                }
                is Action.Loading -> {
                    _listState.value = FridgeListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun remove(id: Int) {
        viewModelScope.launch {
            updateFridgeUseCase(id, true)
        }
    }

}