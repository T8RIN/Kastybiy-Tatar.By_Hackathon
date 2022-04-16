package ru.tech.kastybiy.data.remote.dto

import ru.tech.kastybiy.core.constants.Constants.DELIMETER
import ru.tech.kastybiy.core.constants.Constants.IMG_URL
import ru.tech.kastybiy.domain.model.Cuisine

data class CuisineDto(
    val calories: Double,
    val carboh: Double,
    val category: String,
    val cookSteps: String,
    val itogProducts: String,
    val cookTime: Int,
    val fats: Double,
    val id: Int,
    val products: String,
    val proteins: Double,
    val source: String,
    val title: String
)

fun CuisineDto.toCuisine(): Cuisine =
    Cuisine(
        id,
        products.removePrefix(DELIMETER).split(DELIMETER),
        itogProducts.split(DELIMETER).map { it.toInt() },
        calories,
        carboh,
        category,
        cookSteps.split("\\n"),
        cookTime,
        fats,
        proteins,
        source,
        title,
        IMG_URL.replace(DELIMETER, id.toString())
    )