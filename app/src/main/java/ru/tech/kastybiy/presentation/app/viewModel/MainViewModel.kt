package ru.tech.kastybiy.presentation.app.viewModel

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.tech.kastybiy.core.Action
import ru.tech.kastybiy.domain.model.Product
import ru.tech.kastybiy.domain.use_case.cuisine.get_fridge_list.GetFridgeListUseCase
import ru.tech.kastybiy.domain.use_case.cuisine.get_prod_list.GetProductsListUseCase
import ru.tech.kastybiy.domain.use_case.cuisine.update_fridge.UpdateFridgeUseCase
import ru.tech.kastybiy.presentation.app.components.ProductsListState
import ru.tech.kastybiy.presentation.ui.utils.Screen
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getProductsListUseCase: GetProductsListUseCase,
    private val getFridgeListUseCase: GetFridgeListUseCase,
    private val updateFridgeUseCase: UpdateFridgeUseCase
) : ViewModel() {

    var openSheet by mutableStateOf(false)

    var selectedItem by mutableStateOf(0)
    var title by mutableStateOf(Screen.Cuisine.title)
    val id = mutableStateOf(-1)

    var showProductsDialog by mutableStateOf(false)

    @ExperimentalMaterial3Api
    val scrollBehavior by mutableStateOf(TopAppBarDefaults.pinnedScrollBehavior())

    private val default: ArrayList<Product> = arrayListOf()
    private val _productsList = mutableStateOf(ProductsListState())
    val productsList: State<ProductsListState> = _productsList

    val tempList = mutableStateListOf<Int>()

    init {
        fetchList()
    }

    fun reload() {
        fetchList()
    }

    private fun fetchList() {
        getProductsListUseCase().onEach { result ->
            when (result) {
                is Action.Success -> {
                    _productsList.value =
                        ProductsListState(list = result.data)

                    default.clear()
                    result.data?.let { default.addAll(it) }
                }
                is Action.Empty -> {
                    _productsList.value = ProductsListState(
                        error = result.message ?: "Нәрсәдер начар булып чыккан"
                    )
                }
                is Action.Loading -> {
                    _productsList.value = ProductsListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
        getFridgeListUseCase().onEach { result ->
            if (result is Action.Success && default.isNotEmpty() && result.data != null) {
                _productsList.value =
                    ProductsListState(list = (default - result.data).sortedBy { it.name })
            }
        }.launchIn(viewModelScope)
    }

    fun processToFridge() {
        viewModelScope.launch {
            tempList.forEach {
                updateFridgeUseCase(it, false)
            }
            tempList.clear()
        }
    }
}