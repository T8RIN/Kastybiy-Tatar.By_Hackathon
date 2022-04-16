package ru.tech.kastybiy.presentation.tatar_cuisine.components

import ru.tech.kastybiy.domain.model.Cuisine

data class CuisineState (
    val isLoading: Boolean = false,
    val cuisineList: List<Cuisine>? = null,
    val error: String = ""
)