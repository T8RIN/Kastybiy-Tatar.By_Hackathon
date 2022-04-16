package ru.tech.kastybiy.presentation.app.components

import ru.tech.kastybiy.domain.model.Product

data class ProductsListState(
    val isLoading: Boolean = false,
    val list: List<Product>? = null,
    val error: String = ""
)
