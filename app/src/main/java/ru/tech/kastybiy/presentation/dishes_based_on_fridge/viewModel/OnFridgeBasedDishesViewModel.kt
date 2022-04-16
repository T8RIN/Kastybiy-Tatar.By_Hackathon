package ru.tech.kastybiy.presentation.dishes_based_on_fridge.viewModel

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.tech.kastybiy.core.Action
import ru.tech.kastybiy.domain.model.Cuisine
import ru.tech.kastybiy.domain.use_case.cuisine.get_cuisine.GetCuisineUseCase
import ru.tech.kastybiy.domain.use_case.cuisine.get_fridge_list.GetFridgeListUseCase
import ru.tech.kastybiy.presentation.tatar_cuisine.components.CuisineState
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class OnFridgeBasedDishesViewModel @Inject constructor(
    private val getFridgeListUseCase: GetFridgeListUseCase,
    private val getCuisineUseCase: GetCuisineUseCase
) : ViewModel() {

    @ExperimentalMaterial3Api
    val scrollBehavior by mutableStateOf(TopAppBarDefaults.pinnedScrollBehavior())

    private val _dishes = mutableStateOf(CuisineState())
    val dishes: State<CuisineState> = _dishes

    init {
        fetch()
    }

    private fun fetch() {
        viewModelScope.launch {
            var list: List<Int> = emptyList()
            getFridgeListUseCase().collect { result ->
                if (result is Action.Success) {
                    list = (result.data ?: emptyList()).map { it.id }
                }
                getCuisineUseCase().collect { result1 ->
                    when (result1) {
                        is Action.Success -> {
                            val tmp = result1.data ?: emptyList()
                            val lst = arrayListOf<Cuisine>()

                            tmp.forEach { dish ->
                                var cnt = 0
                                list.forEach { id ->
                                    if (dish.productIds.contains(id)) cnt++
                                }
                                val coeff =
                                    (cnt / dish.productIds.size.toFloat() * 100).roundToInt()
                                if (coeff > 49) {
                                    lst.add(dish)
                                }
                            }

                            _dishes.value = CuisineState(cuisineList = lst)
                        }
                        is Action.Empty -> {
                            _dishes.value = CuisineState(
                                error = result1.message ?: "Нәрсәдер начар булып чыккан"
                            )
                        }
                        is Action.Loading -> {
                            _dishes.value = CuisineState(isLoading = true)
                        }
                    }
                }
            }
        }
    }

}
