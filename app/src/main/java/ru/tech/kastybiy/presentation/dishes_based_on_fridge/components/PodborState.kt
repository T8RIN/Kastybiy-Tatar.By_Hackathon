package ru.tech.kastybiy.presentation.dishes_based_on_fridge.components

import ru.tech.kastybiy.domain.model.Cuisine

data class PodborState(
    val isLoading: Boolean = false,
    val cuisineList: List<Pair<Cuisine, Int>>? = null,
    val error: String = ""
)