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
import ru.tech.kastybiy.domain.use_case.get_cuisine.GetCuisineUseCase
import ru.tech.kastybiy.domain.use_case.get_fridge_list.GetFridgeListUseCase
import ru.tech.kastybiy.presentation.dishes_based_on_fridge.components.PodborState
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class OnFridgeBasedDishesViewModel @Inject constructor(
    private val getFridgeListUseCase: GetFridgeListUseCase,
    private val getCuisineUseCase: GetCuisineUseCase
) : ViewModel() {

    @ExperimentalMaterial3Api
    val scrollBehavior by mutableStateOf(TopAppBarDefaults.pinnedScrollBehavior())

    private val _dishes = mutableStateOf(PodborState())
    val dishes: State<PodborState> = _dishes

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
                            val lst = arrayListOf<Pair<Cuisine, Int>>()

                            tmp.forEach { dish ->
                                var cnt = 0
                                list.forEach { id ->
                                    if (dish.productIds.contains(id)) cnt++
                                }
                                val coeff =
                                    (cnt / dish.productIds.size.toFloat() * 100).roundToInt()
                                if (coeff > 29) {
                                    lst.add(Pair(dish, coeff))
                                }
                            }

                            _dishes.value =
                                PodborState(cuisineList = lst.sortedByDescending { it.second })
                        }
                        is Action.Empty -> {
                            _dishes.value = PodborState(
                                error = result1.message ?: "Нәрсәдер начар булып чыккан"
                            )
                        }
                        is Action.Loading -> {
                            _dishes.value = PodborState(isLoading = true)
                        }
                    }
                }
            }
        }
    }

}
