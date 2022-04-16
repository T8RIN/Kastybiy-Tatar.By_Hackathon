package ru.tech.kastybiy.presentation.products_list.components

import ru.tech.kastybiy.domain.model.Product

data class FridgeListState(
    val isLoading: Boolean = false,
    val products: List<Product>? = null,
    val error: String = ""
)