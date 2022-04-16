package ru.tech.kastybiy.presentation.dish_details.components

import ru.tech.kastybiy.domain.model.Cuisine

data class DishDetailsState(
    val isLoading: Boolean = false,
    val dish: Cuisine? = null,
    val error: String = ""
)